package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.app.dto.FcmTopicDto;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.FcmToken;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.app.dto.FcmMessageDto;
import com.example.toyou.app.dto.FcmRequest;
import com.example.toyou.repository.FcmTokenRepository;
import com.example.toyou.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Value("${spring.firebase.project-id}")
    private String FIREBASE_PROJECT_ID;

    /**
     * FCM Token 저장
     */
    @Transactional
    public void saveToken(Long userId, String token) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        if (fcmTokenRepository.existsByToken(token)) {
            throw new GeneralException(FCM_TOKEN_ALREADY_EXISTING);
        }

        FcmToken newFcmToken = FcmToken.builder()
                .user(user)
                .token(token)
                .build();

        fcmTokenRepository.save(newFcmToken);
    }

    /**
     * FCM Token 갱신
     */
    @Transactional
    public void updateToken(Long userId, String token) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        FcmToken tokenToUpdate = fcmTokenRepository.findByToken(token)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FCM_TOKEN_NOT_FOUND));

        if (tokenToUpdate.getUser() != user) throw new GeneralException(FCM_TOKEN_NOT_MINE);

        tokenToUpdate.setConnectedAt(LocalDateTime.now());
    }

    /**
     * FCM Token 조회
     */
    public FcmResponse.getTokenDto getToken(String nickname) {

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUser(user);

        List<String> tokenList = fcmTokens.stream()
                .map(FcmToken::getToken)
                .toList();

        return FcmResponse.getTokenDto.builder()
                .token(tokenList)
                .build();
    }

    /**
     * FCM Token 삭제
     */
    @Transactional
    public void deleteToken(Long userId, String token) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        FcmToken fcmToken = fcmTokenRepository.findByToken(token)
                .orElseThrow(() -> new GeneralException(FCM_TOKEN_INVALID));

        if (fcmToken.getUser() != user) throw new GeneralException(FCM_TOKEN_NOT_MINE);

        fcmTokenRepository.delete(fcmToken);
    }

    /**
     * FCM 전송(개별 토큰)
     * 푸시 메시지 처리를 수행하는 비즈니스 로직
     */
    public void sendMessageTo(FcmRequest.sendMessageDto fcmRequest) throws IOException {

        String token = fcmRequest.getToken();

        FcmToken fcmToken = fcmTokenRepository.findByToken(token)
                .orElseThrow(() -> new GeneralException(FCM_TOKEN_NOT_FOUND));

        String message = makeMessage(token, fcmRequest.getTitle(), fcmRequest.getBody());
        sendFcmMessage(message, fcmToken);
    }

    // FCM 전송(Topic)
    public void sendMessageToTopic(String topic, String title, String body) throws IOException {
        String message = makeMessageToTopic(topic, title, body);
        sendFcmMessage(message, null);
    }

    private void sendFcmMessage(String message, FcmToken fcmToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // RestTemplate 이용중 클라이언트의 한글 깨짐 방지
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        String API_URL = "https://fcm.googleapis.com/v1/projects/" + FIREBASE_PROJECT_ID + "/messages:send";

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            log.info("FCM 응답: {}", response.getBody());

            // 200 + error:.. 토큰 삭제(ex.앱 삭제)
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            if (jsonResponse.has("error")) {
                log.error("FCM 메시지 전송 실패: {}", jsonResponse.get("error").toString());
                if (fcmToken != null) {
                    fcmTokenRepository.delete(fcmToken);
//                    throw new GeneralException(FCM_TOKEN_INVALID);
                }
            } else {
                log.info("FCM 메시지 전송 성공");
            }

        } catch (Exception e) {
            log.error("FCM 요청 실패: {}", e.getMessage());

            if (fcmToken != null) {
                fcmTokenRepository.delete(fcmToken);
                throw new GeneralException(FCM_TOKEN_INVALID);
            }
        }
    }

    // Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받습니다.
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // FCM 전송 정보(개별 토큰)를 기반으로 메시지를 구성합니다. (Object -> String)
    private String makeMessage(String token, String title, String body) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(token)
                        .notification(FcmMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }

    private String makeMessageToTopic(String topic, String title, String body) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        FcmTopicDto fcmTopicDto = FcmTopicDto.builder()
                .message(FcmTopicDto.Message.builder()
                        .topic(topic)
                        .notification(FcmTopicDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmTopicDto);
    }

    // 30일 동안 접속하지 않은 FCM 토큰 정보 삭제
    @Transactional
    public void cleanUpOldFcmTokens() {
        LocalDateTime limitDate = LocalDateTime.now().minusDays(30);
        fcmTokenRepository.deleteByConnectedAtBefore(limitDate);
    }

    // 유저의 모든 FCM Token 삭제
    @Transactional
    public void deleteAllToken(User user) {

        List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUser(user);

        fcmTokenRepository.deleteAll(fcmTokens);
    }
}
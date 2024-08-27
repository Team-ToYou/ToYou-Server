package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.domain.User;
import com.example.toyou.app.dto.FcmMessageDto;
import com.example.toyou.app.dto.FcmRequest;
import com.example.toyou.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final UserRepository userRepository;

    @Value("${spring.firebase.project-id}")
    private String FIREBASE_PROJECT_ID;

    /**
     * FCM Token 저장
     */
    @Transactional
    public void saveToken(Long userId, String token) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        user.setFcmToken(token);
    }

    /**
     * FCM Token 조회
     */
    @Transactional
    public FcmResponse.getTokenDto getToken(String nickname) {

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return FcmResponse.getTokenDto.builder()
                .token(user.getFcmToken())
                .build();
    }



    // 푸시 메시지 처리를 수행하는 비즈니스 로직
    public void sendMessageTo(FcmRequest.sendMessageDto fcmRequest) throws IOException {

        String message = makeMessage(fcmRequest);
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

        } catch (Exception e) {
            log.error("FCM 요청 실패: {}", e.getMessage());
            throw new GeneralException(FCM_TOKEN_INVALID);
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

    // FCM 전송 정보를 기반으로 메시지를 구성합니다. (Object -> String)
    private String makeMessage(FcmRequest.sendMessageDto fcmRequest) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmRequest.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmRequest.getTitle())
                                .body(fcmRequest.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }
}
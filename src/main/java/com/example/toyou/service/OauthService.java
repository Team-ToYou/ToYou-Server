package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.UserRequest;
import com.example.toyou.domain.OauthInfo;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.OauthProvider;
import com.example.toyou.oauth2.jwt.TokenProvider;
import com.example.toyou.repository.UserRepository;
import com.example.toyou.service.UserService.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Optional;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.INVALID_CODE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    // 카카오 로그인
    @Transactional
    public void kakaoLogin(String oauthAccessToken, HttpServletResponse response) {
        //OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        //oauthId 조회
        String oauthId = responseJson.get("id").asText();

        String accessToken = "";
        String refreshToken = "";

        Optional<User> optionalUser = userRepository.findByOauthInfo_OauthId(oauthId);

        //DB에 회원정보가 있을때 토큰 발급
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            //카카오 액세스 토큰 저장
            user.getOauthInfo().setOauthAccessToken(oauthAccessToken);

            //토큰 생성
            accessToken = tokenProvider.generateToken(user, Duration.ofHours(2), "access");
            refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14), "refresh");
            log.info("access: " + accessToken);
            log.info("refresh : " + refreshToken);

            //Refresh 토큰 저장
            redisService.setValues(user.getId(), refreshToken, Duration.ofDays(14));
        }

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    // 카카오 로그아웃
    @Transactional
    public void kakaoLogout(String refreshToken) {

        //유효 검사
        try {
            tokenProvider.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED); // 만료 검사
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        //토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = tokenProvider.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            log.info("category: {}", category);
            throw new GeneralException(ErrorStatus.DIFFERENT_CATEGORY);
        }

        //토큰에서 유저 조회
        Long userId = tokenProvider.getUserId(refreshToken);
        User user = userService.findById(userId);

        //DB에 저장되어 있는지 확인
        if (!redisService.getValues(userId).equals(refreshToken)) throw new GeneralException(ErrorStatus.TOKEN_INVALID);

        //리프레시 토큰 삭제 from Redis
        redisService.deleteValues(userId);

        //액세스 토큰만 만료 처리(웹 브라우저의 카카오계정 세션은 만료하려면 "카카오계정과 함께 로그아웃" 방식 사용 필요)
        //카카오 액세스 토큰 조회
        String oauthAccessToken = user.getOauthInfo().getOauthAccessToken();

        //HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oauthAccessToken);

        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> logoutRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                logoutRequest,
                String.class
        );

        log.info("logoutResponse: " + response.getBody());
    }

    // 회원 가입
    @Transactional
    public void registerOauthUser(String oauthAccessToken, UserRequest.registerUserDTO request, HttpServletResponse response) {

        //OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        //oauthId 조회
        String oauthId = responseJson.get("id").asText();

        OauthInfo oauthInfo = new OauthInfo(oauthId, OauthProvider.KAKAO, oauthAccessToken);

        // 이미 존재하는 회원 정보인지 검사
        if (userRepository.existsByOauthInfo_OauthId(oauthId)) throw new GeneralException(ErrorStatus.ALREADY_JOINED);

        // 이미 존재하는 닉네임인지 검사
        if (userRepository.existsByNickname(request.getNickname()))
            throw new GeneralException(ErrorStatus.EXISTING_NICKNAME);

        // 유저 정보 저장
        User user = userRepository.findByOauthInfo(oauthInfo)
                .orElse(User.builder()
                        .nickname(request.getNickname())
                        .oauthInfo(oauthInfo)
                        .adConsent(request.isAdConsent())
                        .status(request.getStatus())
                        .build());
        userRepository.save(user);

        //토큰 발급
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2), "access");
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14), "refresh");
        log.info("access: " + accessToken);
        log.info("refresh : " + refreshToken);

        //Refresh 토큰 저장
        redisService.setValues(user.getId(), refreshToken, Duration.ofDays(14));

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    // 인가코드로 카카오 액세스 토큰 요청
    @Transactional
    public String requestAccess(String code, HttpServletRequest request, HttpServletResponse response) {
        return getAccessToken(code);
    }

    /**
     * 인가 코드로 카카오 서버에 액세스 토큰을 요청하는 메서드이다.
     *
     * @param code 인가 코드
     * @return 액세스 토큰
     */
    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    tokenRequest,
                    String.class
            );

            // HTTP 응답에서 액세스 토큰 꺼내기
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(responseBody);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println("kakao_access_token : " + jsonNode.get("access_token").asText());
            return jsonNode.get("access_token").asText();
        } catch (HttpClientErrorException e) {
            throw new GeneralException(INVALID_CODE);
        }
    }

    /**
     * 액세스 토큰으로 카카오 서버에 회원 정보를 요청하는 메서드이다.
     *
     * @param accessToken 액세스 토큰
     * @return JSON 형식의 회원 정보
     */
    private JsonNode getKakaoUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    userInfoRequest,
                    String.class
            );

            // HTTP 응답 반환
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseBody);

        } catch (HttpClientErrorException e) { // HTTP 오류 응답 처리
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) { // 유효한 토큰 X
                throw new GeneralException(ErrorStatus.OAUTH_TOKEN_INVALID);
            }
            throw new RuntimeException("HTTP error occurred: " + e.getStatusCode(), e);
        } catch (JsonProcessingException e) { // JSON 파싱 오류 처리
            throw new RuntimeException("JSON processing error", e);
        }
    }
}


package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.domain.OauthInfo;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.OauthProvider;
import com.example.toyou.oauth2.jwt.TokenProvider;
import com.example.toyou.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

//    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.jwt.header}")
    private String ACCESS_HEADER;

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    @Transactional
    public void kakaoLogin(String oauthAccessToken, HttpServletRequest request, HttpServletResponse response) {
        // 1. OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        // 2. 회원 정보 저장
        User user = registerKakaoUser(responseJson, oauthAccessToken);

        // 3. JWT 액세스 토큰 발급
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        System.out.println("jwt_access_token : " + accessToken);
        response.setHeader(ACCESS_HEADER, accessToken);
    }

    @Transactional
    public void kakaoLoginTest(String code, HttpServletRequest request, HttpServletResponse response) {
        // 1. 인가 코드로 OAuth2 액세스 토큰 요청
        String oauthAccessToken = getAccessToken(code);

        // 2. OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        // 3. 회원 정보 저장
        User user = registerKakaoUser(responseJson, oauthAccessToken);

        // 4. JWT 액세스 토큰 발급
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        System.out.println("jwt_access_token : " + accessToken);
        response.setHeader(ACCESS_HEADER, accessToken);
    }

    /**
     * 인가 코드로 카카오 서버에 액세스 토큰을 요청하는 메서드이다.
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
    }

    /**
     * 액세스 토큰으로 카카오 서버에 회원 정보를 요청하는 메서드이다.
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

    /**
     * 카카오 회원 정보를 데이터베이스에 저장하는 메서드이다.
     * @param responseJson JSON 형식의 카카오 회원 정보
     * @return 저장된 user 객체
     */
    private User registerKakaoUser(JsonNode responseJson, String accessToken) {
        String oauthId = responseJson.get("id").asText();
        JsonNode profile = responseJson.get("kakao_account").get("profile");
        String nickname = profile.get("nickname").asText();
        String profileImage = profile.get("profile_image_url").asText();

        System.out.println("이름 : " + nickname);

        OauthInfo oauthInfo = new OauthInfo(oauthId, OauthProvider.KAKAO);

        User user = userRepository.findByOauthInfo(oauthInfo)
                .map(entity -> entity.updateAccessToken(accessToken))
                .orElse(User.builder()
                        .accessToken(accessToken)
                        .nickname(nickname)
                        .profileImage(profileImage)
                        .oauthInfo(oauthInfo)
                        .build());

        return userRepository.save(user);
    }
}

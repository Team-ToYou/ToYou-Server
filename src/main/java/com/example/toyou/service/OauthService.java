package com.example.toyou.service;

import com.example.toyou.common.apiPayload.code.status.ErrorStatus;
import com.example.toyou.common.apiPayload.exception.GeneralException;
import com.example.toyou.dto.apple.AppleUserInfoResponse;
import com.example.toyou.dto.request.UserRequest;
import com.example.toyou.domain.OauthInfo;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.OauthProvider;
import com.example.toyou.common.jwt.TokenProvider;
import com.example.toyou.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static com.example.toyou.common.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final FcmService fcmService;
    private final AppleService appleService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final String REFRESH_TOKEN_CATEGORY = "refresh";
    private static final String ACCESS_TOKEN_CATEGORY = "access";

    /**
     * 애플 로그인
     */
    @Transactional
    public void appleLogin(String authorizationCode, HttpServletResponse response) throws IOException {
        log.info("[애플 로그인]");
        String accessToken = "";
        String refreshToken = "";

        // 애플 사용자 정보 요청
        AppleUserInfoResponse userInfo = appleService.getAppleUserProfile(authorizationCode);
        String oauthId = userInfo.getSub();

        // DB에서 사용자 확인
        Optional<User> optionalUser = userRepository.findByOauthInfo_OauthId(oauthId);
        log.info("DB에 사용자 존재 여부: {}", optionalUser.isPresent());

        //DB에 회원 정보가 있을때 토큰 발급
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            //토큰 생성
            accessToken = issueAccessToken(user);
            refreshToken = issueRefreshToken(user);
            log.info("access: " + accessToken);
            log.info("refresh : " + refreshToken);
        }

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 카카오 로그인
     */
    @Transactional
    public void kakaoLogin(String oauthAccessToken, HttpServletResponse response) {
        log.info("[카카오 로그인]");
        //OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        //oauthId 조회
        String oauthId = responseJson.get("id").asText();
        log.info("조회된 oauthId: {}", oauthId);

        String accessToken = "";
        String refreshToken = "";

        Optional<User> optionalUser = userRepository.findByOauthInfo_OauthId(oauthId);
        log.info("DB에 사용자 존재 여부: {}", optionalUser.isPresent());

        //DB에 회원정보가 있을때 토큰 발급
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            //카카오 액세스 토큰 저장
            user.getOauthInfo().setOauthAccessToken(oauthAccessToken);

            //토큰 생성
            accessToken = issueAccessToken(user);
            refreshToken = issueRefreshToken(user);
            log.info("access: " + accessToken);
            log.info("refresh : " + refreshToken);
        }

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 카카오 로그아웃
     */
    @Transactional
    public void kakaoLogout(Long userId, String refreshToken) {

        log.info("[카카오 로그아웃]");

        //refresh 토큰 검사 후 유저 id 추출
        Long userIdFromRefresh = checkRefreshToken(refreshToken);

        // access 토큰과 refresh 토큰에 해당하는 유저 유저 비교
        if (!userId.equals(userIdFromRefresh)) throw new GeneralException(TOKEN_INVALID);

        User user = userService.findById(userId);

        //리프레시 토큰 삭제 from Redis
        redisService.deleteValues(userId);

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

    /**
     * 로그아웃 - 리프레시 토큰 제거
     */
    @Transactional
    public void deleteRefresh(Long userId, String refreshToken) {

        log.info("[로그아웃 - 리프레시 토큰 제거]");

        //refresh 토큰 검사 후 유저 id 추출
        Long userIdFromRefresh = checkRefreshToken(refreshToken);

        // access 토큰과 refresh 토큰에 해당하는 유저 비교
        if (!userId.equals(userIdFromRefresh)) throw new GeneralException(TOKEN_INVALID);

        User user = userService.findById(userId);

        //리프레시 토큰 삭제 from Redis
        redisService.deleteValues(userId);
    }

    /**
     * 카카오 회원 가입
     */
    @Transactional
    public void registerOauthUser(String oauthAccessToken, UserRequest.registerUserDTO request, HttpServletResponse response) {
        log.info("[카카오 회원가입]");

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
        String accessToken = issueAccessToken(user);
        String refreshToken = issueRefreshToken(user);
        log.info("access: " + accessToken);
        log.info("refresh : " + refreshToken);

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 애플 회원가입
     */
    @Transactional
    public void registerAppleUser(String authorizationCode, UserRequest.registerUserDTO request, HttpServletResponse response) throws IOException {
        log.info("[애플 회원가입]");

        // 애플 사용자 정보 요청
        AppleUserInfoResponse userInfo = appleService.getAppleUserProfile(authorizationCode);
        String oauthId = userInfo.getSub();
        String appleRefreshToken = userInfo.getRefreshToken();

        // 애플리프레시 토큰을 oauthAccessToken 필드(카카오용)에 저장(추후 분리 필요)
        OauthInfo oauthInfo = new OauthInfo(oauthId, OauthProvider.APPLE, appleRefreshToken);

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
        String accessToken = issueAccessToken(user);
        String refreshToken = issueRefreshToken(user);
        log.info("access: " + accessToken);
        log.info("refresh : " + refreshToken);

        //응답 설정
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 카카오 회원탈퇴
     */
    @Transactional
    public void kakaoUnlink(Long userId, String refreshToken) {

        log.info("[카카오 회원탈퇴]");

        //refresh 토큰 검사 후 유저 id 추출
        Long userIdFromRefresh = checkRefreshToken(refreshToken);

        // access 토큰과 refresh 토큰에 해당하는 유저 유저 비교
        if (!userId.equals(userIdFromRefresh)) throw new GeneralException(TOKEN_INVALID);

        //리프레시 토큰 삭제 from Redis
        redisService.deleteValues(userId);

        User user = userService.findById(userId);

        //유저 FCM Token 전체 삭제
        fcmService.deleteAllToken(user);

        //유저 정보 soft delete
        user.setDeletedAt();

        // 삭제된 유저 정보 업데이트
        userRepository.save(user);

        // 실제로 delete 호출하여 @SQLDelete로 처리
//        userRepository.delete(user);

        //카카오 액세스 토큰 조회
        String oauthAccessToken = user.getOauthInfo().getOauthAccessToken();

        //HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oauthAccessToken);

        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> unlinkRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                unlinkRequest,
                String.class
        );

        log.info("unlinkResponse: " + response.getBody());
    }

    /**
     * 애플 회원탈퇴
     */
    @Transactional
    public void appleUnlink(Long userId, String refreshToken) {

        log.info("[애플 회원탈퇴]");

        //refresh 토큰 검사 후 유저 id 추출
        Long userIdFromRefresh = checkRefreshToken(refreshToken);

        // access 토큰과 refresh 토큰에 해당하는 유저 유저 비교
        if (!userId.equals(userIdFromRefresh)) throw new GeneralException(TOKEN_INVALID);

        //리프레시 토큰 삭제 from Redis
        redisService.deleteValues(userId);

        User user = userService.findById(userId);

        //유저 FCM Token 전체 삭제
        fcmService.deleteAllToken(user);

        //유저 정보 soft delete
        user.setDeletedAt();

        // 삭제된 유저 정보 업데이트
        userRepository.save(user);

        // 실제로 delete 호출하여 @SQLDelete로 처리
//        userRepository.delete(user);

        //애플 리프레시 토큰 조회 및 해지
        String appleRefreshToken = user.getOauthInfo().getOauthAccessToken();
        appleService.revokeToken(appleRefreshToken);
    }

    /**
     * JWT 토큰 재발급
     */
    @Transactional
    public void reissue(String refreshToken, HttpServletResponse response) {

        //refresh 토큰 검사 후 유저 id 추출
        Long userId = checkRefreshToken(refreshToken);
        User user = userService.findById(userId);

        //make new JWT
        String newAccess = issueAccessToken(user);
        String newRefresh = issueRefreshToken(user);

        log.info("[JWT 토큰 재발급]");
        log.info("access: " + newAccess);
        log.info("refresh : " + newRefresh);

        response.setHeader("access_token", newAccess);
        response.setHeader("refresh_token", newRefresh);
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 인가코드로 카카오 액세스 토큰 요청
     */
    @Transactional
    public void requestAccess(String code, HttpServletResponse response) {
        String access = getAccessToken(code);

        response.setHeader("kakao_access", access);
        response.setStatus(HttpStatus.OK.value());
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

            log.info("kakao_access_token : {}", jsonNode.get("access_token").asText());
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

    /**
     * AccessToken 생성
     */
    public String issueAccessToken(User user) {
        return tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION, ACCESS_TOKEN_CATEGORY);
    }

    /**
     * RefreshToken 생성 및 저장
     */
    public String issueRefreshToken(User user) {
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION, REFRESH_TOKEN_CATEGORY);
        redisService.setValues(user.getId(), refreshToken, REFRESH_TOKEN_DURATION);
        return refreshToken;
    }

    /**
     * refresh 토큰 검사 후 유저 id 추출
     */
    private Long checkRefreshToken(String refreshToken) {
        // 유효 검사
        tokenProvider.validateToken(refreshToken);

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = tokenProvider.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            log.info("category: {}", category);
            throw new GeneralException(DIFFERENT_CATEGORY);
        }

        //토큰에서 유저 조회
        Long userId = tokenProvider.getUserId(refreshToken);

        //DB(Redis)에 저장되어 있는지 확인
        if (!redisService.getValues(userId).equals(refreshToken))
            throw new GeneralException(TOKEN_INVALID);

        return userId;
    }
}


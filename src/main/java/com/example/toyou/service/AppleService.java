package com.example.toyou.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.toyou.common.apiPayload.code.status.ErrorStatus;
import com.example.toyou.common.apiPayload.exception.GeneralException;
import com.example.toyou.dto.apple.AppleSocialTokenInfoResponse;
import com.example.toyou.dto.apple.AppleUserInfoResponse;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AppleService {

    private final String APPLE_URL = "https://appleid.apple.com";

    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
    private String APPLE_KEY_PATH;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.service-id}")
    private String APPLE_SERVICE_ID;

    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.key-id}")
    private String APPLE_KEY_ID;

    @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
    private String APPLE_REDIRECT_URL;

    public String getAppleLogin() {
        return APPLE_URL + "/auth/authorize"
                + "?client_id=" + APPLE_SERVICE_ID
                + "&redirect_uri=" + APPLE_REDIRECT_URL
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }

    /**
     * Apple 토큰 인증 API 호출 -> 응답받은 ID 토큰을 JWT Decoding 처리 -> AppleUserInfoResponseDto로 반환
     * @param authorizationCode 사용자로부터 받은 인증 코드
     * @return 디코딩된 사용자 정보를 담고 있는 AppleUserInfoResponseDto 객체
     */
    public AppleUserInfoResponse getAppleUserProfile(String authorizationCode) throws IOException {
        log.info("authorizationCode : {}", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE));

//        String requestBody = "client_id=" + APPLE_CLIENT_ID +
        String requestBody = "client_id=" + APPLE_SERVICE_ID +
                "&client_secret=" + generateClientSecret() +
                "&grant_type=authorization_code" +
                "&code=" + authorizationCode;

        log.info("requestBody : {}", requestBody);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<AppleSocialTokenInfoResponse> response = restTemplate.exchange(
                    APPLE_URL + "/auth/token",
                    HttpMethod.POST,
                    request,
                    AppleSocialTokenInfoResponse.class);

            log.info("response : {}", response.getBody());

            DecodedJWT decodedJWT = JWT.decode(Objects.requireNonNull(response.getBody()).getIdToken());

            return AppleUserInfoResponse.builder()
                    .sub(decodedJWT.getClaim("sub").asString())
                    .email(decodedJWT.getClaim("email").asString())
                    .refreshToken(response.getBody().getRefreshToken())
                    .build();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) { // 유효한 토큰 X
                throw new GeneralException(ErrorStatus.INVALID_CODE);
            }
            throw new RuntimeException("HTTP error occurred: " + e.getStatusCode(), e);
        }
    }

    /**
     * Apple의 인증 서버와의 통신에 사용될 JWT을 생성하기 위해 사용되는 ClientSecret
     * ClientSecret은 토큰 요청 시 서명 목적으로 사용되며, 공개키/비공개키 인증 메커니즘이 포함됨
     * @return 생성된 JWT ClientSecret
     */
    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, APPLE_KEY_ID)
                .setIssuer(APPLE_TEAM_ID)
                .setAudience(APPLE_URL)
//                .setSubject(APPLE_CLIENT_ID)
                .setSubject(APPLE_SERVICE_ID)
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    /**
     * 애플의 JWT 클라이언트 시크릿 생성을 위한 비공개 키 로드
     * @return 로드된 RSA 비공개 키
     */
    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

//        try {
//            byte[] privateKeyBytes = Base64.getDecoder().decode(APPLE_KEY_PATH);
//            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
//            return converter.getPrivateKey(privateKeyInfo);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting private key from String", e);
//        }

        try (InputStream inputStream = new ClassPathResource(APPLE_KEY_PATH).getInputStream();
             PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream))) { // p8 파일을 읽음

            Object object = pemParser.readObject();

            if (object instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                throw new IllegalArgumentException("Invalid private key format");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading private key file", e);
        }
    }

    // 사용자 토큰 해지
    public void revokeToken(String refreshToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", APPLE_CLIENT_ID);
        body.add("client_id", APPLE_SERVICE_ID);
        body.add("token", refreshToken);
        body.add("client_secret", generateClientSecret());
        body.add("token_type_hint", "refresh_token");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://appleid.apple.com/auth/revoke",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            log.info("Apple token revoke response: {}", response.getStatusCode());
        } catch (HttpClientErrorException e) { // HTTP 오류 응답 처리
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) { // 유효한 토큰 X
                throw new GeneralException(ErrorStatus.REFRESH_TOKEN_INVALID);
            }
            throw new RuntimeException("HTTP error occurred: " + e.getStatusCode(), e);
        }
    }

}

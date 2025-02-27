package com.example.toyou.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.toyou.dto.apple.AppleSocialTokenInfoResponse;
import com.example.toyou.dto.apple.AppleUserInfoResponse;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.key-id}")
    private String APPLE_KEY_ID;

    /**
     * Apple 토큰 인증 API 호출 -> 응답받은 ID 토큰을 JWT Decoding 처리 -> AppleUserInfoResponseDto로 반환
     * @param authorizationCode 사용자로부터 받은 인증 코드
     * @return 디코딩된 사용자 정보를 담고 있는 AppleUserInfoResponseDto 객체
     */
    public AppleUserInfoResponse getAppleUserProfile(String authorizationCode) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE));

        String requestBody = "client_id=" + APPLE_CLIENT_ID +
                "&client_secret=" + generateClientSecret() +
                "&grant_type=authorization_code" +
                "&code=" + authorizationCode;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AppleSocialTokenInfoResponse> response = restTemplate.exchange(
                APPLE_URL + "/auth/token",
                HttpMethod.POST,
                request,
                AppleSocialTokenInfoResponse.class);

        DecodedJWT decodedJWT = JWT.decode(Objects.requireNonNull(response.getBody()).getIdToken());

        return AppleUserInfoResponse.builder()
                .sub(decodedJWT.getClaim("sub").asString())
                .email(decodedJWT.getClaim("email").asString())
                .build();
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
                .setSubject(APPLE_CLIENT_ID)
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

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(APPLE_KEY_PATH);
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
}

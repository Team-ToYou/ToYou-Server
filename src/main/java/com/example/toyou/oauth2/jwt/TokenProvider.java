package com.example.toyou.oauth2.jwt;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.TOKEN_EXPIRED;
import static com.example.toyou.apiPayload.code.status.ErrorStatus.TOKEN_INVALID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKeyString; // secretKey는 String으로 저장됨

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes()); // Key 객체로 변환
    }

    public String generateToken(User user, Duration expiredAt, String category) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user, category);
    }

    /**
     * JWT 토큰을 생성하는 메서드이다.
     *
     * @param expiry 토큰의 만료 시간
     * @param user   회원 정보
     * @return 생성된 토큰
     */
    private String makeToken(Date expiry, User user, String category) {
        Date now = new Date();
        System.out.println("Token issued at: " + now);
        System.out.println("Token expires at: " + expiry);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ(타입) : JWT
                .setIssuedAt(now)                               // 내용 iat(발급 일시) : 현재 시간
                .setExpiration(expiry)                          // 내용 exp(만료일시) : expiry 멤버 변수값
                .setSubject(String.valueOf(user.getId()))     // 내용 sub(토큰 제목) : 회원 ID
                .claim("id", user.getId())              // 클레임 id : 회원 ID
                .claim("category", category)  // access or refresh
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // HS256 방식으로 암호화
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메서드이다.
     *
     * @param token 검증할 JWT 토큰
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었음을 명시적으로 처리
            log.info("Expired JWT token: {}", e.getMessage());
            throw new GeneralException(TOKEN_EXPIRED);
        } catch (Exception e) {
            // 서명 오류, 잘못된 토큰 등 기타 JWT 관련 오류를 처리
            log.info("Invalid JWT token: {}", e.getMessage());
            throw new GeneralException(TOKEN_INVALID);
        }
    }

    /**
     * JWT 토큰의 만료 시간을 조회하는 메서드이다.
     *
     * @param token 검증할 JWT 토큰
     */
    public void isExpired(String token) {
        Claims claims = getClaims(token);
        claims.getExpiration();
    }

    /**
     * JWT 토큰에서 카테고리를 추출하는 메서드이다.
     *
     * @param token JWT 토큰
     * @return 카테고리
     */
    public String getCategory(String token) {
        Claims claims = getClaims(token);
        return claims.get("category", String.class);
    }

    /**
     * 토큰에서 회원 ID를 가져오는 메서드이다.
     * @param token JWT 토큰
     * @return 회원 ID
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드이다.
     *
     * @param token 인증된 회원의 토큰
     * @return 인증 정보를 담은 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    /**
     * 주어진 토큰에서 클레임을 조회하는 메서드이다.
     *
     * @param token JWT 토큰
     * @return 클레임 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

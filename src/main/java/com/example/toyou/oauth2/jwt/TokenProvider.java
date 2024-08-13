package com.example.toyou.oauth2.jwt;

import com.example.toyou.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    // Secret Key 객체 생성 (HS256 방식으로 암호화하기 위한 키)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    /**
     * JWT 토큰을 생성하는 메서드이다.
     * @param expiry 토큰의 만료 시간
     * @param user 회원 정보
     * @return 생성된 토큰
     */
    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        System.out.println("Token issued at: " + now);
        System.out.println("Token expires at: " + expiry);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ(타입) : JWT
                .setIssuedAt(now)                               // 내용 iat(발급 일시) : 현재 시간
                .setExpiration(expiry)                          // 내용 exp(만료일시) : expiry 멤버 변수값
                .setSubject(String.valueOf(user.getId()))     // 내용 sub(토큰 제목) : 회원 ID
                .claim("id", user.getId())              // 클레임 id : 회원 ID
                .signWith(key)
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메서드이다.
     * @param token 검증할 JWT 토큰
     * @return 토큰 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰이다.
            return false;
        }
    }

    /**
     * 토큰 기반으로 인증 정보를 가져오는 메서드이다.
     * @param token 인증된 회원의 토큰
     * @return 인증 정보를 담은 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
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
     * 주어진 토큰에서 클레임을 조회하는 메서드이다.
     * @param token JWT 토큰
     * @return 클레임 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

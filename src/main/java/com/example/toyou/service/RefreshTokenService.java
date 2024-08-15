package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.TokenResponse;
import com.example.toyou.domain.RefreshToken;
import com.example.toyou.domain.User;
import com.example.toyou.oauth2.jwt.TokenProvider;
import com.example.toyou.repository.RefreshTokenRepository;
import com.example.toyou.service.HomeService.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse.reissueDTO reissue(String refreshToken) {

        // 유효 검사
        try {
            tokenProvider.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED); // 만료 검사
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = tokenProvider.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            log.info("category: {}", category);
            throw new GeneralException(ErrorStatus.DIFFERENT_CATEGORY);
        }

        //DB에 저장되어 있는지 확인
        RefreshToken existingRefresh = refreshTokenRepository.findByRefresh(refreshToken)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TOKEN_INVALID));

        User user = userService.findById(existingRefresh.getUserId());

        //make new JWT
        String newAccess = tokenProvider.generateToken(user, Duration.ofHours(2), "access");
        String newRefresh = tokenProvider.generateToken(user, Duration.ofDays(14), "refresh");

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByRefresh(refreshToken);
        addRefreshEntity(user, newRefresh, Duration.ofDays(14));

        return new TokenResponse.reissueDTO(newAccess, newRefresh);
    }

    @Transactional
    public void addRefreshEntity(User user, String refresh, Duration expiredAt) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiredAt.toMillis());

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .refresh(refresh)
                .expiration(expirationDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}

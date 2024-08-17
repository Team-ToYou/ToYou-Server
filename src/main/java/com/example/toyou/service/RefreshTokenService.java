package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.TokenResponse;
import com.example.toyou.domain.User;
import com.example.toyou.oauth2.jwt.TokenProvider;
import com.example.toyou.service.UserService.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RedisService redisService;

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

        //토큰에서 유저 조회
        Long userId = tokenProvider.getUserId(refreshToken);
        User user = userService.findById(userId);

        //DB에 저장되어 있는지 확인
        if(!redisService.getValues(userId).equals(refreshToken)) throw new GeneralException(ErrorStatus.TOKEN_INVALID);

        //make new JWT
        String newAccess = tokenProvider.generateToken(user, Duration.ofHours(2), "access");
        String newRefresh = tokenProvider.generateToken(user, Duration.ofDays(14), "refresh");

        //새 Refresh 토큰 저장
        redisService.setValues(userId, newRefresh, Duration.ofDays(14));

        return new TokenResponse.reissueDTO(newAccess, newRefresh);
    }
}

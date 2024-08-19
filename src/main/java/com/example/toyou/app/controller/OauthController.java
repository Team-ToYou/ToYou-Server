package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.TokenResponse;
import com.example.toyou.app.dto.UserRequest;
import com.example.toyou.app.dto.UserResponse;
import com.example.toyou.service.OauthService;
import com.example.toyou.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Auth", description = "Auth 관련 API입니다.")
public class OauthController {

    private final OauthService oauthService;
    private final RefreshTokenService refreshTokenService;

    /**
     * [POST] /auth/kakao
     * 카카오 로그인(프론트용)
     * 카카오 액세스 토큰 받아오는 버전
     * @param token 카카오 측 액세스 토큰
     * @return
     */
    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 측에서 얻은 액세스 토큰으로 보낸 후 jwt 토큰을 헤더로 받아옵니다.")
        public CustomApiResponse kakaoLogin(@RequestHeader String token, HttpServletRequest request, HttpServletResponse response) {
            oauthService.kakaoLogin(token, request, response);
            return CustomApiResponse.onSuccess(null);
    }

    /**
     * [POST] /auth/kakao/access
     * 카카오 액세스 토큰 요청(서버 테스트용)
     * @param code 카카오 측 인가코드
     * @return
     */
    @PostMapping("/kakao/access")
    @Operation(summary = "카카오 액세스 토큰 요청(서버 테스트용)", description = "프론트 사용 X")
    public CustomApiResponse requestKaKaoAccess(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        String access = oauthService.requestAccess(code, request, response);
        response.setHeader("kakao_access", access);

        return CustomApiResponse.onSuccess(null);
    }

    /**
     * [POST] /auth/reissue
     * JWT 토큰 재발급
     * @param refreshToken
     * @return
     */
    @PostMapping("/reissue")
    @Operation(summary = "JWT 토큰 재발급", description = "기존 refresh 토큰을 보낸 후 새로운 access & refresh 토큰을 받아옵니다.")
    public CustomApiResponse reissue(@RequestHeader String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        TokenResponse.reissueDTO tokenResponse = refreshTokenService.reissue(refreshToken);

        response.setHeader("access_token", tokenResponse.getAccessToken());
        response.setHeader("refresh_token", tokenResponse.getRefreshToken());

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "oauthId를 통해 회원가입합니다.")
    public CustomApiResponse<?> registerOauthUser(@RequestHeader String oauthId,
                                                  @RequestBody @Valid UserRequest.registerUserDTO request, HttpServletResponse response) {

        oauthService.registerOauthUser(oauthId, request, response);

        return CustomApiResponse.onSuccess(null);
    }
}

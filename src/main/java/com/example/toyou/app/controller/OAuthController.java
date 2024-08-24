package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.TokenResponse;
import com.example.toyou.app.dto.UserRequest;
import com.example.toyou.service.OauthService;
import com.example.toyou.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Auth", description = "Auth 관련 API입니다.")
public class OAuthController {

    private final OauthService oauthService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰을 통해 access 토큰과 refresh 토큰을 발급 받습니다.")
        public CustomApiResponse<?> kakaoLogin(@RequestHeader String oauthAccessToken, HttpServletResponse response) {
            oauthService.kakaoLogin(oauthAccessToken, response);
            return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/kakao/access")
    @Operation(summary = "카카오 액세스 토큰 요청(서버 테스트용)", description = "프론트 사용 X")
    public CustomApiResponse<?> requestKaKaoAccess(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        String access = oauthService.requestAccess(code, request, response);
        response.setHeader("kakao_access", access);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT 토큰 재발급", description = "refresh 토큰을 통해 새로운 access 토큰과 refresh 토큰을 발급 받습니다.")
    public CustomApiResponse<?> reissue(@RequestHeader String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        TokenResponse.reissueDTO tokenResponse = refreshTokenService.reissue(refreshToken);

        response.setHeader("access_token", tokenResponse.getAccessToken());
        response.setHeader("refresh_token", tokenResponse.getRefreshToken());

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "카카오 액세스 토큰을 통해 회원가입합니다.")
    public CustomApiResponse<?> registerOauthUser(@RequestHeader String oauthAccessToken,
                                                  @RequestBody @Valid UserRequest.registerUserDTO request, HttpServletResponse response) {

        oauthService.registerOauthUser(oauthAccessToken, request, response);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "refresh 토큰을 통해 로그아웃을 진행합니다.")
    public CustomApiResponse<?> logout(@RequestHeader String refreshToken) {

        oauthService.kakaoLogout(refreshToken);

        return CustomApiResponse.onSuccess(null);
    }
}

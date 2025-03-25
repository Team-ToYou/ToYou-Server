package com.example.toyou.controller;

import com.example.toyou.common.apiPayload.CustomApiResponse;
import com.example.toyou.dto.apple.AppleUserInfoResponse;
import com.example.toyou.dto.request.UserRequest;
import com.example.toyou.dto.response.AuthResponse;
import com.example.toyou.service.AppleService;
import com.example.toyou.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Auth", description = "Auth 관련 API입니다.")
public class OAuthController {

    private final OauthService oauthService;

    @PostMapping("/apple")
    @Operation(summary = "애플 로그인", description = "authorization code을 통해 사용자 인증 후, access 토큰과 refresh 토큰을 발급 받습니다.")
    public CustomApiResponse<AuthResponse.appleLoginDTO> appleLogin(@RequestHeader String authorizationCode) throws IOException {
        AuthResponse.appleLoginDTO response = oauthService.appleLogin(authorizationCode);
        return CustomApiResponse.onSuccess(response);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰을 통해 access 토큰과 refresh 토큰을 발급 받습니다.")
    public CustomApiResponse<?> kakaoLogin(@RequestHeader String oauthAccessToken, HttpServletResponse response) {
        oauthService.kakaoLogin(oauthAccessToken, response);
        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/kakao/access")
    @Operation(summary = "카카오 액세스 토큰 요청(서버 테스트용)", description = "프론트 사용 X")
    public CustomApiResponse<?> requestKaKaoAccess(@RequestParam String code, HttpServletResponse response) {
        oauthService.requestAccess(code, response);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT 토큰 재발급", description = "refresh 토큰을 통해 새로운 access 토큰과 refresh 토큰을 발급 받습니다.")
    public CustomApiResponse<?> reissue(@RequestHeader String refreshToken, HttpServletResponse response) {
        oauthService.reissue(refreshToken, response);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/signup")
    @Operation(summary = "카카오 회원가입", description = "카카오 액세스 토큰을 통해 회원가입합니다.")
    public CustomApiResponse<?> registerOauthUser(@RequestHeader String oauthAccessToken,
                                                  @RequestBody @Valid UserRequest.registerUserDTO request, HttpServletResponse response) {

        oauthService.registerOauthUser(oauthAccessToken, request, response);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/signup/apple")
    @Operation(summary = "애플 회원가입", description = "애플 회원가입을 진행합니다.")
    public CustomApiResponse<?> registerAppleUser(Principal principal,
                                                  @RequestBody @Valid UserRequest.registerUserDTO request) {

        Long userId = Long.parseLong(principal.getName());

        oauthService.registerAppleUser(userId, request);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/logout")
    @Operation(summary = "카카오 로그아웃", description = "access 토큰과 refresh 토큰을 통해 로그아웃을 진행합니다.")
    public CustomApiResponse<?> logout(Principal principal, @RequestHeader String refreshToken) {

        Long userId = Long.parseLong(principal.getName());

        oauthService.kakaoLogout(userId, refreshToken);

        return CustomApiResponse.onSuccess(null);
    }

    @PostMapping("/logout/apple")
    @Operation(summary = "애플 로그아웃", description = "access 토큰과 refresh 토큰을 통해 로그아웃을 진행합니다.")
    public CustomApiResponse<?> appleLogout(Principal principal, @RequestHeader String refreshToken) {

        Long userId = Long.parseLong(principal.getName());

        oauthService.deleteRefresh(userId, refreshToken);

        return CustomApiResponse.onSuccess(null);
    }

    @DeleteMapping("/unlink")
    @Operation(summary = "카카오 회원탈퇴", description = "access 토큰과 refresh 토큰을 통해 회원탈퇴를 진행합니다.")
    public CustomApiResponse<?> unlink(Principal principal, @RequestHeader String refreshToken) {

        Long userId = Long.parseLong(principal.getName());

        oauthService.kakaoUnlink(userId, refreshToken);

        return CustomApiResponse.onSuccess(null);
    }

    @DeleteMapping("/unlink/apple")
    @Operation(summary = "애플 회원탈퇴", description = "access 토큰과 refresh 토큰을 통해 회원탈퇴를 진행합니다.")
    public CustomApiResponse<?> appleUnlink(Principal principal, @RequestHeader String refreshToken) {

        Long userId = Long.parseLong(principal.getName());

        oauthService.appleUnlink(userId, refreshToken);

        return CustomApiResponse.onSuccess(null);
    }
}

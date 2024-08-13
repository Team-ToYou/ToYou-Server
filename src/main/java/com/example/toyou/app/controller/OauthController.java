package com.example.toyou.app.controller;

import com.example.toyou.service.OauthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Oauth", description = "Oauth 관련 API입니다.")
public class OauthController {

    private final OauthService oauthService;

    /**
     * [POST] /auth/kakao
     * 카카오 로그인(프론트용)
     * 카카오 액세스 토큰 받아오는 버전
     * @param token 카카오 측 액세스 토큰
     * @return
     */
    @PostMapping("/kakao")
        public ResponseEntity<Void> kakaoLogin(@RequestHeader String token, HttpServletRequest request, HttpServletResponse response) {
            oauthService.kakaoLogin(token, request, response);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * [POST] /auth/kakao/test
     * 카카오 로그인(서버 테스트용)
     * 인가코드 받아오는 버전
     * @param code 카카오 측 인가코드
     * @return
     */
    @PostMapping("/kakao/test")
    public ResponseEntity<Void> kakaoLoginTest(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        oauthService.kakaoLoginTest(code, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

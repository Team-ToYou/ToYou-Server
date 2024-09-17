package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.FcmRequest;
import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
@Validated
@Tag(name = "FCM", description = "FCM 관련 API입니다.")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/token")
    @Operation(summary = "FCM 토큰 저장", description = "사용자의 고유한 FCM 토큰을 저장합니다.")
    public CustomApiResponse<?> saveToken(Principal principal, @RequestBody @Valid FcmRequest.saveTokenDto fcmRequest) {

        Long userId = Long.parseLong(principal.getName());

        fcmService.saveToken(userId, fcmRequest.getToken());

        return CustomApiResponse.onSuccess(null);
    }

    @GetMapping("/token")
    @Operation(summary = "FCM 토큰 조회", description = "닉네임을 통해 해당 사용자의 FCM 토큰을 조회합니다.")
    public CustomApiResponse<FcmResponse.getTokenDto> getToken(@RequestParam String keyword) {

        FcmResponse.getTokenDto token = fcmService.getToken(keyword);

        return CustomApiResponse.onSuccess(token);
    }

    @PostMapping("/send")
    @Operation(summary = "FCM 전송", description = "대상 디바이스로 FCM(푸시 메세지)을 전송합니다.")
    public CustomApiResponse<?> send(@RequestBody @Valid FcmRequest.sendMessageDto fcmRequest) throws IOException {

        fcmService.sendMessageTo(fcmRequest);

        return CustomApiResponse.onSuccess(null);
    }

    @DeleteMapping("/token")
    @Operation(summary = "FCM 토큰 삭제", description = "저장된 토큰 정보를 삭제합니다.")
    public CustomApiResponse<?> deleteToken(Principal principal, @RequestBody @Valid FcmRequest.deleteTokenDto fcmRequest) {

        Long userId = Long.parseLong(principal.getName());

        fcmService.deleteToken(userId, fcmRequest.getToken());

        return CustomApiResponse.onSuccess(null);
    }
}

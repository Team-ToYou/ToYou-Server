package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.*;
import com.example.toyou.domain.User;
import com.example.toyou.jwt.TokenProvider;
import com.example.toyou.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "USER", description = "USER 관련 API입니다.")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/home")
    @Operation(summary = "홈 화면 조회", description = "홈 화면에 나타나는 유저의 정보를 조회합니다.")
    public CustomApiResponse<HomeResponse.GetHomeDTO> getHome(Principal principal){

        Long userId = Long.parseLong(principal.getName());

        HomeResponse.GetHomeDTO getHomeDTO = userService.getHome(userId);

        return CustomApiResponse.onSuccess(getHomeDTO);
    }

    @PostMapping("/emotions")
    @Operation(summary = "감정우표 선택", description = "금일 감정우표를 선택합니다.")
    public CustomApiResponse<?> postEmotion(Principal principal,
                                   @RequestBody @Valid HomeRequest.postEmotionDTO request){

        Long userId = Long.parseLong(principal.getName());

        userService.postEmotion(userId, request);

        return CustomApiResponse.onSuccess(null);
    }

    @GetMapping("/nickname/check")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 여부를 체크합니다.")
    public CustomApiResponse<UserResponse.checkUserNicknameDTO> checkUserNickname(@RequestParam String nickname){

        UserResponse.checkUserNicknameDTO exists = userService.checkUserNickname(nickname);

        return CustomApiResponse.onSuccess(exists);
    }

    @PatchMapping("/nickname")
    @Operation(summary = "닉네임 수정", description = "닉네임을 수정합니다.")
    public CustomApiResponse<?> updateNickname(Principal principal, @RequestBody UserRequest.updateNicknameDTO request){

        Long userId = Long.parseLong(principal.getName());

        userService.updateNickname(userId, request.getNickname());

        return CustomApiResponse.onSuccess(null);
    }

    @PatchMapping("/status")
    @Operation(summary = "현재 상태 수정", description = "현재 상태를 수정합니다.")
    public CustomApiResponse<?> updateStatus(Principal principal, @RequestBody UserRequest.updateStatusDTO request){

        Long userId = Long.parseLong(principal.getName());

        userService.updateStatus(userId, request.getStatus());

        return CustomApiResponse.onSuccess(null);
    }

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 조회", description = "마이페이지에 나타나는 유저 정보를 조회합니다.")
    public CustomApiResponse<UserResponse.GetMyPageDTO> getMyPage(Principal principal){

        Long userId = Long.parseLong(principal.getName());

        UserResponse.GetMyPageDTO myPageInfo = userService.getMyPage(userId);

        return CustomApiResponse.onSuccess(myPageInfo);
    }

    @GetMapping("/test/{userId}")
    @Operation(summary = "액세스 토큰 발급(테스트용)", description = "해당 userId의 액세스 토큰 발급(테스트용)")
    public String getToken(@PathVariable Long userId){

        User user = userService.findById(userId);
        String token = tokenProvider.generateToken(user, Duration.ofDays(14), "access");

        log.info("액세스 토큰 발급: userId={}", userId);

        return token;
    }
}

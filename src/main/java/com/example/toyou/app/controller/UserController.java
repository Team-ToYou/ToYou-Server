package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.*;
import com.example.toyou.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "USER", description = "USER 관련 API입니다.")
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    @Operation(summary = "홈 화면 조회", description = "홈 화면에 나타나는 유저의 정보를 조회합니다.")
    public CustomApiResponse<HomeResponse.GetHomeDTO> getHome(@RequestHeader Long userId){

        HomeResponse.GetHomeDTO getHomeDTO = userService.getHome(userId);

        log.info("홈화면 조회: userId={}", userId);

        return CustomApiResponse.onSuccess(getHomeDTO);
    }

    @PostMapping("/emotions")
    @Operation(summary = "감정우표 선택", description = "금일 감정우표를 선택합니다.")
    public CustomApiResponse<?> postEmotion(@RequestHeader Long userId,
                                   @RequestBody @Valid HomeRequest.postEmotionDTO request){

        userService.postEmotion(userId, request);

        log.info("감정우표 선택: emotion={}", request.getEmotion());

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
    public CustomApiResponse<?> updateNickname(@RequestHeader Long userId, @RequestBody UserRequest.updateNicknameDTO request){

        userService.updateNickname(userId, request.getNickname());

        log.info("닉네임 수정: nickname={}", request.getNickname());

        return CustomApiResponse.onSuccess(null);
    }

    @PatchMapping("/status")
    @Operation(summary = "현재 상태 수정", description = "현재 상태를 수정합니다.")
    public CustomApiResponse<?> updateStatus(@RequestHeader Long userId, @RequestBody UserRequest.updateStatusDTO request){

        userService.updateStatus(userId, request.getStatus());

        log.info("현재 상태 수정: status={}", request.getStatus());

        return CustomApiResponse.onSuccess(null);
    }

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 조회", description = "마이페이지에 나타나는 유저 정보를 조회합니다.")
    public CustomApiResponse<UserResponse.GetMyPageDTO> getMyPage(@RequestHeader Long userId){

        UserResponse.GetMyPageDTO myPageInfo = userService.getMyPage(userId);

        return CustomApiResponse.onSuccess(myPageInfo);
    }
}

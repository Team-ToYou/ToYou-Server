package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.service.HomeService.UserService;
import com.example.toyou.app.dto.HomeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Validated
@Slf4j
public class HomeController {

    private final UserService userService;

    /**
     * [GET] /home
     * 홈 화면 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    public ApiResponse<HomeResponse.GetHomeDTO> getHome(@RequestHeader Long userId){

        HomeResponse.GetHomeDTO getHomeDTO = userService.getHome(userId);

        log.info("홈화면 조회: userId={}", userId);

        return ApiResponse.onSuccess(getHomeDTO);
    }

    /**
     * [PATCH] /home/emotions
     * 감정우표 선택
     * @param userId 유저 식별자
     * @param request
     * @return
     */
    @PatchMapping("/emotions")
    public ApiResponse postEmotion(@RequestHeader Long userId,
                                   @RequestBody @Valid HomeRequest.postEmotionDTO request){

        userService.updateEmotion(userId, request);

        log.info("감정우표 선택: emotion={}", request.getEmotion());

        return ApiResponse.onSuccess(null);
    }

//    @PatchMapping("/reset")
//    public ApiResponse resetEmotions(){
//
//        userService.resetTodayEmotion();
//
//        return ApiResponse.onSuccess(null);
//    }
}

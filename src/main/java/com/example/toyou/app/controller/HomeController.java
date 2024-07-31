package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.service.HomeService.UserService;
import com.example.toyou.app.dto.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Home 컨트롤러", description = "Home 관련 API입니다.")
public class HomeController {

    private final UserService userService;

    /**
     * [GET] /home
     * 홈 화면 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    @Operation(summary = "홈 화면 조회", description = "홈 화면에 나타나는 유저의 정보를 조회합니다.")
    public CustomApiResponse<HomeResponse.GetHomeDTO> getHome(@RequestHeader Long userId){

        HomeResponse.GetHomeDTO getHomeDTO = userService.getHome(userId);

        log.info("홈화면 조회: userId={}", userId);

        return CustomApiResponse.onSuccess(getHomeDTO);
    }

    /**
     * [PATCH] /home/emotions
     * 감정우표 선택
     * @param userId 유저 식별자
     * @param request
     * @return
     */
    @PatchMapping("/emotions")
    @Operation(summary = "감정우표 선택", description = "금일 감정우표를 선택합니다.")
    public CustomApiResponse postEmotion(@RequestHeader Long userId,
                                   @RequestBody @Valid HomeRequest.postEmotionDTO request){

        userService.updateEmotion(userId, request);

        log.info("감정우표 선택: emotion={}", request.getEmotion());

        return CustomApiResponse.onSuccess(null);
    }

//    @PatchMapping("/reset")
//    public CustomApiResponse resetEmotions(){
//
//        userService.resetTodayEmotion();
//
//        return CustomApiResponse.onSuccess(null);
//    }
}

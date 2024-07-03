package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.service.HomeService.UserService;
import com.example.toyou.app.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

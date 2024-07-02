package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.domain.User;
import com.example.toyou.service.HomeService.HomeService;
import com.example.toyou.app.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Validated
public class HomeController {

//    private final HomeService homeService;

    /**
     * [GET] /alarms
     * 홈 화면 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    public ApiResponse<HomeResponse.GetHomeDTO> getHome(@RequestHeader Long userId){

//        User user = userService.get


//
//        List<Alarm> alarmList = alarmService.getAlarmList(member.getId());
//
//        log.info("프로필 알람 데이터 조회: user={}", user.getId());
//
//        return ApiResponse.onSuccess(AlarmConverter.toGetAlarmListDTO(alarmList));
        return null;
    }
}

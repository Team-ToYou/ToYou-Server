package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.AlarmResponse;
import com.example.toyou.service.AlarmService.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * [GET] /alarms
     * 알림 목록 조회
     * @param userId 사용자 식별자
     * @return
     */
    @GetMapping
    public ApiResponse<AlarmResponse.getAlarmsDTO> getAlarms(@RequestHeader Long userId){

        AlarmResponse.getAlarmsDTO alarms = alarmService.getAlarms(userId);

        return ApiResponse.onSuccess(alarms);
    }

    /**
     * [DELETE] /alarms/{alarmId}
     * 알림 삭제
     * @param userId 유저 식별자
     * @param alarmId 알림 식별자
     * @return
     */
    @DeleteMapping("/{alarmId}")
    public ApiResponse deleteFriendRequest(@RequestHeader Long userId,
                                           @PathVariable Long alarmId) {

        alarmService.deleteAlarm(userId, alarmId);

        return ApiResponse.onSuccess(null);
    }
}

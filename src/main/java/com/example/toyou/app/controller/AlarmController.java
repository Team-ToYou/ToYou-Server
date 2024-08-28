package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.AlarmResponse;
import com.example.toyou.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Alarm", description = "Alarm 관련 API입니다.")
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * [GET] /alarms
     * 알림 목록 조회
     * @param userId 사용자 식별자
     * @return
     */
    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "유저의 알림 목록을 조회합니다.")
    public CustomApiResponse<AlarmResponse.getAlarmsDTO> getAlarms(@RequestHeader Long userId){

        AlarmResponse.getAlarmsDTO alarms = alarmService.getAlarms(userId);

        return CustomApiResponse.onSuccess(alarms);
    }

    /**
     * [DELETE] /alarms/{alarmId}
     * 알림 삭제
     * @param userId 유저 식별자
     * @param alarmId 알림 식별자
     * @return
     */
    @DeleteMapping("/{alarmId}")
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    public CustomApiResponse deleteFriendRequest(@RequestHeader Long userId,
                                           @PathVariable Long alarmId) {

        alarmService.deleteAlarm(userId, alarmId);

        return CustomApiResponse.onSuccess(null);
    }
}

package com.example.toyou.controller;

import com.example.toyou.global.response.CustomApiResponse;
import com.example.toyou.dto.response.AlarmResponse;
import com.example.toyou.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Alarm", description = "Alarm 관련 API입니다.")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "유저의 알림 목록을 조회합니다.")
    public CustomApiResponse<AlarmResponse.getAlarmsDTO> getAlarms(Principal principal){

        Long userId = Long.parseLong(principal.getName());

        AlarmResponse.getAlarmsDTO alarms = alarmService.getAlarms(userId);

        return CustomApiResponse.onSuccess(alarms);
    }

    @DeleteMapping("/{alarmId}")
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    public CustomApiResponse<?> deleteFriendRequest(Principal principal,
                                                    @PathVariable Long alarmId) {

        Long userId = Long.parseLong(principal.getName());

        alarmService.deleteAlarm(userId, alarmId);

        return CustomApiResponse.onSuccess(null);
    }
}
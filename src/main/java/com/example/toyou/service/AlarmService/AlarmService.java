package com.example.toyou.service.AlarmService;

import com.example.toyou.app.dto.AlarmResponse;
import com.example.toyou.app.dto.CardResponse;

public interface AlarmService {

    AlarmResponse.getAlarmsDTO getAlarms(Long userId);

    void deleteAlarm(Long userId, Long alarmId);
}

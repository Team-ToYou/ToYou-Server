package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AlarmResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAlarmsDTO {
        private List<AlarmResponse.alarmInfo> alarmList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class alarmInfo {
        private Long alarmId;
        private String content;
        private String nickname;
        private AlarmType alarmType;
    }
}

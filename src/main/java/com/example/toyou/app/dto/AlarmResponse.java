package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.AlarmType;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "알림 식별자", nullable = false, example = "1")
        private Long alarmId;
        @Schema(description = "알림 내용", nullable = false, example = "짱구님이 친구 요청을 보냈습니다.")
        private String content;
        @Schema(description = "관련 유저 닉네임", nullable = false, example = "짱구")
        private String nickname;
        @Schema(description = "알림 유형", nullable = false, example = "FRIEND_REQUEST")
        private AlarmType alarmType;
    }
}

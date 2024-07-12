package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HomeResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHomeDTO {

        private Emotion emotion;
        private Integer questionNum;
        private Long cardId;
        private boolean uncheckedAlarm;
    }
}

package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import lombok.Getter;

public class HomeRequest {

    @Getter
    public static class postEmotionDTO {
        private Emotion emotion;
    }
}

package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class HomeRequest {

    @Getter
    public static class postEmotionDTO {
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
    }
}

package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequest {

    @Getter
    public static class registerUserDTO {

        @Schema(description = "마케팅 활용 / 광고성 정보 수신동의", nullable = false, example = "true")
        private boolean adConsent;

        @Schema(description = "닉네임", nullable = false, example = "짱구")
        @Size(max = 15, message = "닉네임은 최대 15자까지 입력할 수 있습니다.")
        private String nickname;

        @Schema(description = "현재 상태", nullable = false, example = "SCHOOL")
        private Status status;
    }

    @Getter
    public static class updateNicknameDTO {

        @Schema(description = "닉네임", nullable = false, example = "짱구")
        @Size(max = 15, message = "닉네임은 최대 15자까지 입력할 수 있습니다.")
        private String nickname;
    }

    @Getter
    public static class updateStatusDTO {

        @Schema(description = "현재 상태", nullable = false, example = "SCHOOL")
        private Status status;
    }
}

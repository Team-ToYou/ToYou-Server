package com.example.toyou.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class FriendRequestRequest {

    @Getter
    public static class createFriendRequestDTO{
        @NotEmpty
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
    }

    @Getter
    public static class deleteFriendRequestDTO{
        @NotEmpty
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
    }

    @Getter
    public static class acceptFriendRequestDTO{
        @NotEmpty
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
    }
}

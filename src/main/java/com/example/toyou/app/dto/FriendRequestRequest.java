package com.example.toyou.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class FriendRequestRequest {

    @Getter
    public static class createFriendRequestDTO{
        @NotNull
        @Schema(description = "유저 ID", nullable = false, example = "1")
        private Long userId;
    }

    @Getter
    public static class deleteFriendRequestDTO{
        @NotNull
        @Schema(description = "유저 ID", nullable = false, example = "1")
        private Long userId;
    }

    @Getter
    public static class acceptFriendRequestDTO{
        @NotNull
        @Schema(description = "유저 ID", nullable = false, example = "1")
        private Long userId;
    }
}

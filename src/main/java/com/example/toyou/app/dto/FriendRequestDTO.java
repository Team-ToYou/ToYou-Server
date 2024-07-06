package com.example.toyou.app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public class FriendRequestDTO {

    @Getter
    public static class createFriendRequestDTO{
        @NotEmpty
        private String nickname;
    }

    @Getter
    public static class deleteFriendRequestDTO{
        @NotEmpty
        private String nickname;
    }
}

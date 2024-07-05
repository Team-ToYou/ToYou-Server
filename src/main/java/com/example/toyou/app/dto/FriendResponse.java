package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FriendResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFriendsDTO {
        private List<friendInfo> friendList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class friendInfo {

        private String nickname;
        private Emotion emotion;
        private String ment;
    }
}

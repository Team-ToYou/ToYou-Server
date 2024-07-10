package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.FriendStatus;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class searchFriendDTO {

        private String nickname;
        private FriendStatus friendStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getFriendYesterdayDTO {

        private List<yesterdayInfo> yesterday;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class yesterdayInfo {

        private Long cardId;
        private String nickname;
    }
}

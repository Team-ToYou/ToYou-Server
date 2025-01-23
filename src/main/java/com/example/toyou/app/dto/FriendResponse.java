package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.FriendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "유저 ID", nullable = false, example = "1")
        private Long userId;
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
        @Schema(description = "멘트", nullable = false, example = "더없이 행복한 하루였어요")
        private String ment;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class searchFriendDTO {
        @Schema(description = "유저 ID", nullable = false, example = "1")
        private Long userId;
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
        @Schema(description = "친구 관계 유형", nullable = false, example = "REQUEST_SENT")
        private FriendStatus friendStatus;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getFriendRequestsDto {

        private List<senderInfo> senderInfos;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class senderInfo {

        @Schema(description = "유저 id", nullable = false, example = "1")
        private Long userId;

        @Schema(description = "닉네임", nullable = false, example = "훈이")
        private String nickname;
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

        @Schema(description = "카드 식별자", nullable = false, example = "1")
        private Long cardId;
        @Schema(description = "닉네임", nullable = false, example = "철수")
        private String nickname;
    }
}

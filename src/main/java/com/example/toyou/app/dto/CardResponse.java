package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class CardResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createCardDTO {

        @Schema(description = "카드 식별자", nullable = false, example = "1")
        private Long cardId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getCardDTO {
        @Schema(description = "날짜", nullable = false, example = "2024-07-31")
        private LocalDate date;
        @Schema(description = "수신자(카드 주인)", nullable = false, example = "짱구")
        private String receiver;
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
        @Schema(description = "공개 여부", nullable = false, example = "true")
        private boolean exposure;

        private List<questionInfo> questionList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionInfo {

        @Schema(description = "질문 식별자", nullable = false, example = "1")
        private Long questionId;
        @Schema(description = "질문 내용", nullable = false, example = "짜장면 vs 짬뽕")
        private String content;
        @Schema(description = "질문 유형", nullable = false, example = "OPTIONAL")
        private QuestionType questionType;
        @Schema(description = "질문자(닉네임)", nullable = false, example = "훈이")
        private String questioner;
        @Schema(description = "답변 내용", nullable = false, example = "짜장면")
        private String answer;
        @Schema(description = "답변 선택란", nullable = true, example = "[\"짜장면\", \"짬뽕\"]")
        private List<String> answerOption;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMyCardsDTO {
        private List<myCardInfo> cardList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myCardInfo {

        @Schema(description = "카드 식별자", nullable = false, example = "1")
        private Long cardId;
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
        @Schema(description = "날짜", nullable = false, example = "2024-07-31")
        private LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getFriendsCardsDTO {
        private List<friendsCardInfo> cardList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class friendsCardInfo {
        @Schema(description = "카드 개수", nullable = false, example = "5")
        private Long cardNum;
        @Schema(description = "날짜", nullable = false, example = "2024-07-31")
        private LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getDailyFriendsCardsDTO {
        private List<dailyFriendsCardInfo> cardList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class dailyFriendsCardInfo {
        @Schema(description = "일기카드 식별자", nullable = false, example = "1")
        private Long cardId;
        @Schema(description = "닉네임", nullable = false, example = "맹구")
        private String nickname;
        @Schema(description = "감정", nullable = false, example = "HAPPY")
        private Emotion emotion;
    }
}

package com.example.toyou.app.dto;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
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
        private Long cardId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getCardDTO {
        private LocalDate date;
        private String receiver;
        private Emotion emotion;
        private List<questionInfo> questionList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class questionInfo {

        private Long questionId;
        private String content;
        private QuestionType questionType;
        private String questioner;
        private String answer;
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
        private Long cardId;
        private Emotion emotion;
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
        private Long cardNum;
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
        private Long cardId;
        private String nickname;
        private Emotion emotion;
    }
}

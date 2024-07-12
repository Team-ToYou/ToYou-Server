package com.example.toyou.converter;

import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    public static DiaryCard toCard(User user, boolean exposure) {
        return DiaryCard.builder()
                .user(user)
                .exposure(exposure)
                .emotion(user.getTodayEmotion())
                .build();
    }

    public static CardResponse.createCardDTO toCreateCardDTO(Long cardId) {
        return CardResponse.createCardDTO.builder()
                .cardId(cardId)
                .build();
    }

    public static CardResponse.getCardDTO toGetCardDTO(DiaryCard card) {
        List<Question> questions = card.getQuestionList();

        List<CardResponse.questionInfo> questionInfo = questions.stream()
                .map(question -> {
                    List<String> answerOptionContents = question.getAnswerOptionList().stream()
                            .map(AnswerOption::getContent)
                            .collect(Collectors.toList());

                    return CardResponse.questionInfo.builder()
                            .questionId(question.getId())
                            .content(question.getContent())
                            .questionType(question.getQuestionType())
                            .questioner(question.getQuestioner())
                            .answer(question.getAnswer())
                            .answerOption(answerOptionContents)
                            .build();
                })
                .toList();

        return CardResponse.getCardDTO.builder()
                .date(card.getCreatedAt().toLocalDate())
                .receiver(card.getUser().getNickname())
                .emotion(card.getEmotion())
                .questionList(questionInfo)
                .build();
    }

    public static CardResponse.getMyCardsDTO toGetMyCardsDTO(List<DiaryCard> myCards) {

        List<CardResponse.myCardInfo> myCardInfos = myCards.stream()
                .map(card -> CardResponse.myCardInfo.builder()
                        .cardId(card.getId())
                        .emotion(card.getEmotion())
                        .date(card.getCreatedAt().toLocalDate())
                        .build())
                .toList();

        return CardResponse.getMyCardsDTO.builder()
                .cardList(myCardInfos)
                .build();
    }
}

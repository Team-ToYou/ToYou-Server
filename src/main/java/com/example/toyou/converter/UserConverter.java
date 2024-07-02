package com.example.toyou.converter;

import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;
import com.example.toyou.app.dto.HomeResponse;

import java.time.LocalDate;

public class UserConverter {

    public static HomeResponse.GetHomeDTO toGetHomeDTO(User user){
        Long cardId = null;
        LocalDate today = LocalDate.now();

        for (DiaryCard card : user.getDiaryCardList()) {
            if (today.equals(card.getCreatedAt().toLocalDate())) {
                cardId = card.getId();
                break;
            }
        }

        return HomeResponse.GetHomeDTO.builder()
                .emotion(user.getTodayEmotion())
                .questionNum(user.getQuestionList().size())
                .cardId(cardId)
                .build();
    }
}

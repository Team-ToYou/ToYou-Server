package com.example.toyou.converter;

import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;
import com.example.toyou.app.dto.HomeResponse;

import java.time.LocalDate;

public class UserConverter {

    public static HomeResponse.GetHomeDTO toGetHomeDTO(User user, Long cardId, int questionNum){
        return HomeResponse.GetHomeDTO.builder()
                .emotion(user.getTodayEmotion())
                .questionNum(questionNum)
                .cardId(cardId)
                .build();
    }
}

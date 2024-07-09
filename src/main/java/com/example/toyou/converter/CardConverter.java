package com.example.toyou.converter;

import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;

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
}

package com.example.toyou.service.CardService;

import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;

public interface CardService {

    CardResponse.createCardDTO createCard(Long userId, CardRequest.createCardDTO request);

    CardResponse.getCardDTO getCard(Long userId, Long cardId);

    void updateCard(Long userId, Long cardId, CardRequest.updateCardDTO request);

    CardResponse.getMyCardsDTO getMyCards(Long userId, int year, int month);
}

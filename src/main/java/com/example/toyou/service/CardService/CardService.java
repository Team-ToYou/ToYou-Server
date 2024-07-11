package com.example.toyou.service.CardService;

import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;

public interface CardService {

    CardResponse.createCardDTO createCard(Long userId, CardRequest.createCardDTO request);

    CardResponse.getCardDTO getCard(Long userId, Long cardId);
}

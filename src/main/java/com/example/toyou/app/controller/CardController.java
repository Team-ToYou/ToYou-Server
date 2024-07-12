package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.service.CardService.CardService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diarycards")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CardController {

    private final CardService cardService;

    /**
     * [POST] /diarycards
     * 일기카드 생성
     * @param userId 유저 식별자
     * @return
     */
    @PostMapping
    public ApiResponse<CardResponse.createCardDTO> createQuestion(@RequestHeader Long userId,
                                                    @RequestBody @Valid CardRequest.createCardDTO request) {

        CardResponse.createCardDTO card = cardService.createCard(userId, request);

        return ApiResponse.onSuccess(card);
    }

    /**
     * [GET] /diarycards/{cardId}
     * 일기카드 상세 조회
     * @param userId 사용자 식별자
     * @param cardId 카드 식별자
     * @return
     */
    @GetMapping("/{cardId}")
    public ApiResponse<CardResponse.getCardDTO> getCard(@RequestHeader Long userId, @PathVariable Long cardId){

        CardResponse.getCardDTO card = cardService.getCard(userId, cardId);

        return ApiResponse.onSuccess(card);
    }

    /**
     * [PATCH] /diarycards/{cardId}
     * 일기카드 수정
     * @param userId 유저 식별자
     * @param cardId 카드 식별자
     * @return
     */
    @PatchMapping("/{cardId}")
    public ApiResponse createQuestion(@RequestHeader Long userId,
                                      @PathVariable Long cardId,
                                      @RequestBody @Valid CardRequest.updateCardDTO request) {

        cardService.updateCard(userId, cardId, request);

        return ApiResponse.onSuccess(null);
    }

    /**
     * [GET] /diarycards/mine
     * 내 일기카드 목록 조회
     * @param userId 사용자 식별자
     * @param year 연도
     * @param month 월
     * @return
     */
    @GetMapping("/mine")
    public ApiResponse<CardResponse.getMyCardsDTO> getCard(@RequestHeader Long userId,
                                                           @RequestParam int year,
                                                           @RequestParam int month){

        CardResponse.getMyCardsDTO cards = cardService.getMyCards(userId, year, month);

        return ApiResponse.onSuccess(cards);
    }
}

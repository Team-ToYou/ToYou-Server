package com.example.toyou.controller;

import com.example.toyou.common.apiPayload.CustomApiResponse;
import com.example.toyou.dto.request.CardRequest;
import com.example.toyou.dto.response.CardResponse;
import com.example.toyou.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/diarycards")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Card", description = "Card 관련 API입니다.")
public class CardController {

    private final CardService cardService;

    @PostMapping
    @Operation(summary = " 일기카드 생성", description = " 일기카드를 생성합니다.")
    public CustomApiResponse<CardResponse.createCardDTO> createQuestion(Principal principal,
                                                                        @RequestBody @Valid CardRequest.createCardDTO request) {

        Long userId = Long.parseLong(principal.getName());

        CardResponse.createCardDTO card = cardService.createCard(userId, request);

        return CustomApiResponse.onSuccess(card);
    }

    @GetMapping("/{cardId}")
    @Operation(summary = "일기카드 상세 조회", description = "특정 일기카드에 대한 상세 조회를 진행합니다.")
    public CustomApiResponse<CardResponse.getCardDTO> getCard(Principal principal, @PathVariable Long cardId) {

        Long userId = Long.parseLong(principal.getName());

        CardResponse.getCardDTO card = cardService.getCard(userId, cardId);

        return CustomApiResponse.onSuccess(card);
    }

    @PatchMapping("/{cardId}")
    @Operation(summary = "일기카드 수정", description = "일기카드를 수정합니다.")
    public CustomApiResponse<?> createQuestion(Principal principal,
                                            @PathVariable Long cardId,
                                            @RequestBody @Valid CardRequest.updateCardDTO request) {

        Long userId = Long.parseLong(principal.getName());

        cardService.updateCard(userId, cardId, request);

        return CustomApiResponse.onSuccess(null);
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "일기카드 삭제", description = "일기카드를 삭제합니다.")
    public CustomApiResponse<?> deleteCard(Principal principal, @PathVariable Long cardId) {

        Long userId = Long.parseLong(principal.getName());

        cardService.deleteCard(userId, cardId);

        return CustomApiResponse.onSuccess(null);
    }

    @PatchMapping("/{cardId}/exposure")
    @Operation(summary = "일기카드 공개여부 전환", description = "일기카드 공개여부를 전환합니다.")
    public CustomApiResponse<CardResponse.toggleExposureDTO> toggleExposure(Principal principal,
                                               @PathVariable Long cardId) {

        Long userId = Long.parseLong(principal.getName());

        CardResponse.toggleExposureDTO exposure = cardService.toggleExposure(userId, cardId);

        return CustomApiResponse.onSuccess(exposure);
    }

    @GetMapping("/mine")
    @Operation(summary = "내 일기카드 목록 조회", description = "내 일기카드 목록을 날짜 별로 조회합니다.")
    public CustomApiResponse<CardResponse.getMyCardsDTO> getCard(Principal principal,
                                                                 @RequestParam int year,
                                                                 @RequestParam int month) {

        Long userId = Long.parseLong(principal.getName());

        CardResponse.getMyCardsDTO cards = cardService.getMyCards(userId, year, month);

        return CustomApiResponse.onSuccess(cards);
    }

    @GetMapping("/friends")
    @Operation(summary = "친구 일기카드 목록 조회", description = "친구들의 일기카드를 날짜 별로 조회합니다.")
    public CustomApiResponse<?> getCard(Principal principal,
                                        @RequestParam int year,
                                        @RequestParam int month,
                                        @RequestParam(required = false) Integer day) {

        Long userId = Long.parseLong(principal.getName());

        if (day == null) {
            CardResponse.getFriendsCardsDTO cards = cardService.getFriendsCards(userId, year, month);
            return CustomApiResponse.onSuccess(cards);
        } else {
            CardResponse.getDailyFriendsCardsDTO dailyCards = cardService.getDailyFriendsCards(userId, year, month, day);
            return CustomApiResponse.onSuccess(dailyCards);
        }
    }

    @GetMapping("/yesterday")
    @Operation(summary = "어제 일기카드 목록 조회", description = "어제 날짜 기준으로 생성된 친구들의 일기카드를 조회합니다.")
    public CustomApiResponse<CardResponse.getYesterdayCardsDto> getYesterdayCards(Principal principal) {

        Long userId = Long.parseLong(principal.getName());

        CardResponse.getYesterdayCardsDto cards = cardService.getYesterdayCards(userId);

        return CustomApiResponse.onSuccess(cards);
    }
}

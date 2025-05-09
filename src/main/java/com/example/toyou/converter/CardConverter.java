package com.example.toyou.converter;

import com.example.toyou.dto.response.CardResponse;
import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
                .exposure(card.isExposure())
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

    public static CardResponse.getFriendsCardsDTO toGetFriendsCardsDTO(List<DiaryCard> friendsCards) {

        // diaryCard를 일별로 그룹화하고 각 그룹의 사이즈를 구함
        Map<LocalDate, Long> dailyCardCounts = friendsCards.stream()
                .collect(Collectors.groupingBy(
                        card -> card.getCreatedAt().toLocalDate(),
                        Collectors.counting() // 각 그룹의 사이즈(count)를 계산
                ));

        // friendsCardInfo 리스트 생성
        List<CardResponse.friendsCardInfo> friendsCardInfos = dailyCardCounts.entrySet().stream()
                .map(entry -> CardResponse.friendsCardInfo.builder()
                        .date(entry.getKey())
                        .cardNum(entry.getValue())
                        .build())
                .toList();

        return CardResponse.getFriendsCardsDTO.builder()
                .cardList(friendsCardInfos)
                .build();
    }

    public static CardResponse.getDailyFriendsCardsDTO toGetDailyFriendsCardsDTO(List<DiaryCard> friendsCards) {

        List<CardResponse.dailyFriendsCardInfo> dailyFriendsCardInfos = friendsCards.stream()
                .map(card -> CardResponse.dailyFriendsCardInfo.builder()
                        .cardId(card.getId())
                        .nickname(card.getUser().getNickname())
                        .emotion(card.getEmotion())
                        .build())
                .toList();

        return CardResponse.getDailyFriendsCardsDTO.builder()
                .cardList(dailyFriendsCardInfos)
                .build();
    }

    public static CardResponse.getYesterdayCardsDto toYesterdayCardsDTO(List<User> friends) {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<CardResponse.yesterdayCardInfo> cardList =  friends.stream()
                .flatMap(friend -> friend.getDiaryCardList().stream()
                        .filter(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday))
                        .filter(DiaryCard::isExposure)
                        .map(diaryCard -> CardResponse.yesterdayCardInfo.builder()
                                .cardId(diaryCard.getId())
                                .cardContent(toGetCardDTO(diaryCard)) //카드 내용
                                .build()
                        ))
                .toList();

        return CardResponse.getYesterdayCardsDto.builder().cards(cardList).build();
    }
}

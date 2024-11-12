package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.converter.CardConverter;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.CardRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
import com.example.toyou.service.FriendService;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final QuestionRepository questionRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;
    private final BadWordFiltering badWordFiltering;

    /**
     * 일기카드 생성
     */
    @Transactional
    public CardResponse.createCardDTO createCard(Long userId, CardRequest.createCardDTO request) {

        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 유저의 일기카드 목록 가져오기
        List<DiaryCard> diaryCards = user.getDiaryCardList();

        // 최근 일기카드와 오늘 날짜 비교
        if (!diaryCards.isEmpty()) {
            DiaryCard latestCard = diaryCards.get(diaryCards.size() - 1); // 가장 최근 일기카드
            LocalDate today = LocalDate.now();
            if (latestCard.getCreatedAt().toLocalDate().equals(today)) {
                throw new GeneralException(ErrorStatus.DUPLICATE_CARD_FOR_TODAY);
            }
        }

        // 일기카드 생성
        DiaryCard newCard = CardConverter.toCard(user, request.isExposure());
        cardRepository.save(newCard);

        List<Question> questionList = newCard.getQuestionList();

        // 1.qaList에서 각 qa 객체의 questionId를 사용하여 Question을 검색
        // 2.answer를 수정 & 일기카드 연동
        request.getQuestionList()
                .forEach(qa -> {
                    Question question = questionRepository.findById(qa.getQuestionId())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_FOUND));

                    // 선택형
                    if (question.getQuestionType() == QuestionType.OPTIONAL) {
                        boolean validAnswer = question.getAnswerOptionList().stream()
                                .anyMatch(option -> option.getContent().equals(qa.getAnswer()));
                        if(!validAnswer) throw new GeneralException(ErrorStatus.NOT_IN_OPTIONS);
                    }

                    String safeContent = badWordFiltering.change(qa.getAnswer(), new String[]{" ", ",", ".", "!", "?", "@", "1"});

                    question.setAnswer(safeContent);
                    question.setDiaryCard(newCard);

                    questionList.add(question);
                });

        return CardConverter.toCreateCardDTO(newCard.getId());
    }

    /**
     * 일기카드 상세 조회
     */
    public CardResponse.getCardDTO getCard(Long userId, Long cardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(!card.isExposure() && (user != card.getUser())) throw new GeneralException(ErrorStatus.PRIVATE_CARD);

        if(card.getUser().isDeleted()) throw new GeneralException(ErrorStatus.CARD_NOT_FOUND);

        return CardConverter.toGetCardDTO(card);
    }

    /**
     * 일가카드 수정
     */
    @Transactional
    public void updateCard(Long userId, Long cardId, CardRequest.updateCardDTO request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(user != card.getUser()) throw new GeneralException(ErrorStatus.NOT_OWNER);

        List<Question> questionList = card.getQuestionList();
        questionList.forEach(question -> question.setDiaryCard(null));
        questionList.clear();

        // 1.qaList에서 각 qa 객체의 questionId를 사용하여 Question을 검색
        // 2.answer를 수정 & 일기카드 연동
        request.getQuestionList()
                .forEach(qa -> {
                    Question question = questionRepository.findById(qa.getQuestionId())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_NOT_FOUND));

                    // 선택형
                    if (question.getQuestionType() == QuestionType.OPTIONAL) {
                        boolean validAnswer = question.getAnswerOptionList().stream()
                                .anyMatch(option -> option.getContent().equals(qa.getAnswer()));
                        if(!validAnswer) throw new GeneralException(ErrorStatus.NOT_IN_OPTIONS);
                    }

                    String safeContent = badWordFiltering.change(qa.getAnswer(), new String[]{" ", ",", ".", "!", "?", "@", "1"});

                    question.setAnswer(safeContent);
                    question.setDiaryCard(card);

                    questionList.add(question);
                });

        card.setExposure(request.isExposure()); // 공개 여부 설정
    }

    /**
     * 일가카드 삭제
     */
    @Transactional
    public void deleteCard(Long userId, Long cardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(user != card.getUser()) throw new GeneralException(ErrorStatus.NOT_OWNER);

       cardRepository.delete(card);
    }

    /**
     * 일가카드 공개여부 전환
     */
    @Transactional
    public CardResponse.toggleExposureDTO toggleExposure(Long userId, Long cardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(user != card.getUser()) throw new GeneralException(ErrorStatus.NOT_OWNER);

        card.toggleExposure();

        return CardResponse.toggleExposureDTO.builder()
                .exposure(card.isExposure())
                .build();
    }

    /**
     * 내 일기카드 목록 조회
     */
    public CardResponse.getMyCardsDTO getMyCards(Long userId, int year, int month) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<DiaryCard> myCards = cardRepository.findByUser(user);

        // 연도와 월을 기준으로 다이어리 카드를 필터링
        List<DiaryCard> filteredCards = myCards.stream()
                .filter(card -> {
                    // card.getDate()는 다이어리 카드의 날짜를 반환하는 메소드라고 가정
                    LocalDate cardDate = card.getCreatedAt().toLocalDate();
                    return cardDate.getYear() == year && cardDate.getMonthValue() == month;
                })
                .toList();

        return CardConverter.toGetMyCardsDTO(filteredCards);
    }

    /**
     * 친구 일기카드 조회(월별)
     */
    public CardResponse.getFriendsCardsDTO getFriendsCards(Long userId, int year, int month) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<User> friends = friendService.getFriendList(user);

        List<DiaryCard> friendsCards = friends.stream()
                .flatMap(friend -> friend.getDiaryCardList().stream()) // 각 친구의 diaryCard 리스트를 flatMap으로 평면화
                .toList();

        // 연도와 월을 기준으로 다이어리 카드를 필터링
        List<DiaryCard> filteredCards = friendsCards.stream()
                .filter(card -> {
                    // card.getDate()는 다이어리 카드의 날짜를 반환하는 메소드라고 가정
                    LocalDate cardDate = card.getCreatedAt().toLocalDate();
                    return cardDate.getYear() == year && cardDate.getMonthValue() == month;
                })
                .filter(DiaryCard::isExposure)
                .toList();

        return CardConverter.toGetFriendsCardsDTO(filteredCards);
    }

    /**
     * 친구 일기카드 조회(일별)
     */
    public CardResponse.getDailyFriendsCardsDTO getDailyFriendsCards(Long userId, int year, int month, int day) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 리스트 조회
        List<User> friends = friendService.getFriendList(user);

        List<DiaryCard> friendsCards = friends.stream()
                .flatMap(friend -> friend.getDiaryCardList().stream()) // 각 친구의 diaryCard 리스트를 flatMap으로 평면화
                .toList();

        // 연도와 월을 기준으로 다이어리 카드를 필터링
        List<DiaryCard> filteredCards = friendsCards.stream()
                .filter(card -> {
                    // card.getDate()는 다이어리 카드의 날짜를 반환하는 메소드라고 가정
                    LocalDate cardDate = card.getCreatedAt().toLocalDate();
                    return cardDate.getYear() == year && cardDate.getMonthValue() == month && cardDate.getDayOfMonth() == day;
                })
                .filter(DiaryCard::isExposure)
                .toList();

        return CardConverter.toGetDailyFriendsCardsDTO(filteredCards);
    }
}

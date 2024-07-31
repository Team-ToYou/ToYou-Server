package com.example.toyou.service.CardService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.converter.CardConverter;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.CardRepository;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final FriendRepository friendRepository;
    private final QuestionRepository questionRepository;

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

                    question.setAnswer(qa.getAnswer());
                    question.setDiaryCard(newCard);
                });

        return CardConverter.toCreateCardDTO(newCard.getId());
    }

    public CardResponse.getCardDTO getCard(Long userId, Long cardId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(!card.isExposure() && (user != card.getUser())) throw new GeneralException(ErrorStatus.PRIVATE_CARD);

        return CardConverter.toGetCardDTO(card);
    }

    @Transactional
    public void updateCard(Long userId, Long cardId, CardRequest.updateCardDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        DiaryCard card = cardRepository.findById(cardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CARD_NOT_FOUND));

        if(user != card.getUser()) throw new GeneralException(ErrorStatus.NOT_OWNER);

        List<Question> questionList = card.getQuestionList();
        questionList.forEach(question -> question.setDiaryCard(null)); // 질문 목록의 각 질문에 대해 카드 ID를 null로 설정
        questionList.clear(); // 카드의 질문 목록 비우기

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

                    question.setAnswer(qa.getAnswer());
                    question.setDiaryCard(card);
                });

        card.setExposure(request.isExposure()); // 공개 여부 설정
    }

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

    public CardResponse.getFriendsCardsDTO getFriendsCards(Long userId, int year, int month) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests1 = friendRepository.findByUserAndAcceptedTrue(user);
        List<FriendRequest> friendRequests2 = friendRepository.findByFriendAndAcceptedTrue(user);

        // 두 리스트 합치기
        List<User> friends = Stream.concat(
                        friendRequests1.stream().map(FriendRequest::getFriend),
                        friendRequests2.stream().map(FriendRequest::getUser)
                )
                .distinct() // 중복 제거
                .toList();

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

    public CardResponse.getDailyFriendsCardsDTO getDailyFriendsCards(Long userId, int year, int month, int day) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests1 = friendRepository.findByUserAndAcceptedTrue(user);
        List<FriendRequest> friendRequests2 = friendRepository.findByFriendAndAcceptedTrue(user);

        // 두 리스트 합치기
        List<User> friends = Stream.concat(
                        friendRequests1.stream().map(FriendRequest::getFriend),
                        friendRequests2.stream().map(FriendRequest::getUser)
                )
                .distinct() // 중복 제거
                .toList();

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

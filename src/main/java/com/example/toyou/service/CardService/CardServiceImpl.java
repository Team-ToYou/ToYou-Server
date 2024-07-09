package com.example.toyou.service.CardService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.converter.CardConverter;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.AnswerOptionRepository;
import com.example.toyou.repository.CardRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
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
public class CardServiceImpl implements CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
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
}

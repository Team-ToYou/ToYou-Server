package com.example.toyou.service;

import com.example.toyou.app.dto.CardRequest;
import com.example.toyou.app.dto.CardResponse;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.CardRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CardServiceTest {

    @Autowired private CardRepository cardRepository;
    @Autowired private CardService cardService;
    @Autowired private UserRepository userRepository;
    @Autowired private QuestionRepository questionRepository;

    private static final Logger logger = LoggerFactory.getLogger(CardServiceTest.class);

    @Test
    @DisplayName("일기카드 공개여부를 전환합니다.")
    void toggleExposure() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        DiaryCard card = DiaryCard.builder()
                .exposure(false)
                .user(user)
                .build();

        cardRepository.save(card);

        assertFalse(card.isExposure(), "일기카드 공개여부 설정 실패");

        // when
        cardService.toggleExposure(user.getId(), card.getId());

        // then
        assertTrue(card.isExposure(), "일기카드 공개여부 전환에 실패했습니다.");
    }

    @Test
    @DisplayName("일기카드 생성 성공")
    void createCardTest() {

        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        Question question1 = Question.builder()
                .user(user)
                .questioner("questioner1")
                .content("content1")
                .answer("answer1")
                .questionType(QuestionType.SHORT_ANSWER)
                .build();
        questionRepository.save(question1);

        Question question2 = Question.builder()
                .user(user)
                .questioner("questioner2")
                .content("content2")
                .answer("answer2")
                .questionType(QuestionType.SHORT_ANSWER)
                .build();
        questionRepository.save(question2);

        CardRequest.createCardDTO createDto = CardRequest.createCardDTO.builder()
                .exposure(true)
                .questionList(new ArrayList<>(List.of(
                        CardRequest.qa.builder()
                                .questionId(question1.getId())
                                .answer("answer1")
                                .build(),
                        CardRequest.qa.builder()
                                .questionId(question2.getId())
                                .answer("answer2")
                                .build()
                )))
                .build();

        // when
        CardResponse.createCardDTO createCardDTO = cardService.createCard(user.getId(), createDto);

        // then
        Long cardId = createCardDTO.getCardId();
        DiaryCard createdCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new AssertionError("생성된 카드가 없습니다."));

        // 카드의 질문 목록 확인
        List<Question> createdQuestions = createdCard.getQuestionList();
        assertEquals(2, createdQuestions.size(), "카드에 연결된 질문의 수가 잘못되었습니다.");

        // 질문 내용 검증
        assertEquals(question1.getId(), createdQuestions.get(0).getId(), "첫 번째 질문이 잘못되었습니다.");
        assertEquals("answer1", createdQuestions.get(0).getAnswer(), "첫 번째 질문의 답변이 잘못되었습니다.");
        assertEquals(question2.getId(), createdQuestions.get(1).getId(), "두 번째 질문이 잘못되었습니다.");
        assertEquals("answer2", createdQuestions.get(1).getAnswer(), "두 번째 질문의 답변이 잘못되었습니다.");
    }

    @Test
    @DisplayName("일기카드 수정 성공")
    void updateCardTest() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        Question question1 = Question.builder()
                .user(user)
                .questioner("questioner1")
                .content("content1")
                .answer("answer1")
                .questionType(QuestionType.SHORT_ANSWER)
                .build();
        questionRepository.save(question1);

        Question question2 = Question.builder()
                .user(user)
                .questioner("questioner2")
                .content("content2")
                .answer("answer2")
                .questionType(QuestionType.SHORT_ANSWER)
                .build();
        questionRepository.save(question2);

        Question question3 = Question.builder()
                .user(user)
                .questioner("questioner3")
                .content("content3")
                .answer("answer3")
                .questionType(QuestionType.SHORT_ANSWER)
                .build();
        questionRepository.save(question3);

        CardRequest.updateCardDTO updateDto = CardRequest.updateCardDTO.builder()
                .exposure(true)
                .questionList(new ArrayList<>(List.of(
                        CardRequest.qa.builder()
                                .questionId(question2.getId())
                                .answer("answer2.1")
                                .build(),
                        CardRequest.qa.builder()
                                .questionId(question3.getId())
                                .answer("answer3")
                                .build()
                )))
                .build();

        List<Question> questionList = Arrays.asList(question1, question2);

        DiaryCard card = DiaryCard.builder()
                .exposure(false)
                .user(user)
                .emotion(Emotion.NORMAL)
                .questionList(new ArrayList<>(questionList))
                .build();

        cardRepository.save(card);

        logger.info("수정 전, questionList: {}", card.getQuestionList().stream()
                .map(Question::getId)
                .collect(Collectors.toList()));

        // when
        cardService.updateCard(user.getId(), card.getId(), updateDto);

        // then
        DiaryCard updatedCard = cardRepository.findById(card.getId()).orElseThrow(
                () -> new AssertionError("Updated card not found")
        );

        logger.info("수정 후, questionList: {}", updatedCard.getQuestionList().stream()
                .map(Question::getId)
                .collect(Collectors.toList()));

        // DB에 질문이 남아 있는지 체크
        assertNotNull(questionRepository.findById(question1.getId()).orElse(null), "question1이 DB에 없습니다.");
        assertNotNull(questionRepository.findById(question2.getId()).orElse(null), "question2가 DB에 없습니다.");
        assertNotNull(questionRepository.findById(question3.getId()).orElse(null), "question3이 DB에 없습니다.");

        assertEquals(question2.getId(), updatedCard.getQuestionList().get(0).getId(), "question2가 첫 번째 인덱스에 없습니다.");
        assertEquals("answer2.1", updatedCard.getQuestionList().get(0).getAnswer(), "question2의 답변이 변경되지 않았습니다.");
        assertEquals(question3.getId(), updatedCard.getQuestionList().get(1).getId(), "질문 목록에 question3이 없습니다.");
    }
}
package com.example.toyou.service;

import com.example.toyou.common.apiPayload.code.status.ErrorStatus;
import com.example.toyou.common.apiPayload.exception.GeneralException;
import com.example.toyou.dto.response.FcmResponse;
import com.example.toyou.dto.request.QuestionRequest;
import com.example.toyou.dto.response.QuestionResponse;
import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.converter.QuestionConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.AlarmRepository;
import com.example.toyou.repository.AnswerOptionRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
import com.example.toyou.util.NicknameUtils;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final BadWordFiltering badWordFiltering;
    private final NicknameUtils nicknameUtils;

    /**
     * 질문 생성
     */
    @Transactional
    public FcmResponse.getMyNameDto createQuestion(Long userId, QuestionRequest.createQuestionDTO request) {

        log.info("[질문 생성] userId={}, targetId={}", userId, request.getTargetId());

        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String questioner = user.getNickname();

        // 질문 대상 검색
        User target = userRepository.findById(request.getTargetId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (user == target) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 익명 선택시
        if (request.isAnonymous()) {
            questioner = nicknameUtils.generateRandomNickname();
        }

        QuestionType questionType = request.getQuestionType();


        String safeContent = badWordFiltering.change(request.getContent(), new String[]{" ", ",", ".", "!", "?", "@", "1"});
        log.info("생성된 질문 내용: {}", safeContent);
        Question newQuestion = QuestionConverter.toQuestion(target, questionType, questioner, safeContent);

        questionRepository.save(newQuestion);

        log.info("생성된 질문 ID: {}", newQuestion.getId());

        // 선택형 일때만 답변 선택란 입력 가능
        if (questionType != QuestionType.OPTIONAL && request.getAnswerOptionList() != null)
            throw new GeneralException(ErrorStatus.INCORRECT_QUESTION_TYPE);

        // 선택형
        if (questionType == QuestionType.OPTIONAL) {

            if (request.getAnswerOptionList() == null) throw new GeneralException(ErrorStatus.EMPTIED_LIST);

            // answerOptionList를 AnswerOption 객체 리스트로 변환
            List<AnswerOption> answerOptions = QuestionConverter.toAnswerOptionList(request.getAnswerOptionList(), newQuestion);

            answerOptionRepository.saveAll(answerOptions);
        }

        // 알림 생성
        Alarm newAlarm = AlarmConverter.toNewQuestionAlarm(questioner, target);

        alarmRepository.save(newAlarm);

        return FcmResponse.getMyNameDto.builder()
                .myName(questioner)
                .build();
    }

    /**
     * 질문 목록 조회
     */
    public QuestionResponse.GetQuestionsDTO getQuestions(Long userId) {

        log.info("[질문 목록 조회] userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Question> questions = questionRepository.findByUserAndCreatedAtBetween(user, startOfDay, endOfDay);

        log.info("조회된 질문 개수: {}", questions.size());

        return QuestionConverter.toGetQuestionDTO(questions);
    }

    // 질문 삭제
    @Transactional
    public void deleteOldQuestions() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();

        List<Question> questionsToDelete = questionRepository.findByCreatedAtBeforeAndDiaryCardIsNull(startOfDay);

        log.info("삭제될 질문 개수: {}", questionsToDelete.size());

        for (Question question : questionsToDelete) {
            question.deleteMappings(); // 연관 관계 해제
            questionRepository.delete(question);
        }
    }

    // 투유 질문 자체 생성
    @Transactional
    public void autoCreateQuestion(QuestionRequest.createQuestionDTO request) {

        // 질문 대상 검색
        User target = userRepository.findById(request.getTargetId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        QuestionType questionType = request.getQuestionType();

        Question newQuestion = QuestionConverter.toQuestion(target, questionType, "투유", request.getContent());

        questionRepository.save(newQuestion);

        log.info("questionType={}", request.getQuestionType());
        log.info("list={}", request.getAnswerOptionList());

        // 선택형
        if (questionType == QuestionType.OPTIONAL) {

            if (request.getAnswerOptionList() == null) throw new GeneralException(ErrorStatus.EMPTIED_LIST);

            // answerOptionList를 AnswerOption 객체 리스트로 변환
            List<AnswerOption> answerOptions = QuestionConverter.toAnswerOptionList(request.getAnswerOptionList(), newQuestion);

            answerOptionRepository.saveAll(answerOptions);
        }
    }
}

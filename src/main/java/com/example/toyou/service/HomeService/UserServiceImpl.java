package com.example.toyou.service.HomeService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.app.dto.HomeResponse;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.converter.QuestionConverter;
import com.example.toyou.converter.UserConverter;
import com.example.toyou.domain.*;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.AlarmRepository;
import com.example.toyou.repository.CustomQuestionRepository;
import com.example.toyou.repository.UserRepository;
import com.example.toyou.service.QuestionService.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomQuestionRepository customQuestionRepository;
    private final QuestionService questionService;

    /**
     * 홈화면 조회
     */
    public HomeResponse.GetHomeDTO getHome(Long userId){

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 금일 생성한 일기카드 조회
        LocalDate today = LocalDate.now();

        Long cardId = null;
        for (DiaryCard card : user.getDiaryCardList()) {
            if (today.equals(card.getCreatedAt().toLocalDate())) {
                cardId = card.getId();
                break;
            }
        }

        // 금일 받은 질문 갯수 조회
        int questionNum = 0;
        for (Question question : user.getQuestionList()) {
            if (today.equals(question.getCreatedAt().toLocalDate())) {
                questionNum++;
            }
        }

        // 체크하지 않은 알림 조회
        boolean uncheckedAlarm = false;
        for (Alarm alarm : user.getAlarmList()) {
            if (!alarm.isChecked()) {
                uncheckedAlarm = true;
            }
        }

        return UserConverter.toGetHomeDTO(user, cardId, questionNum, uncheckedAlarm);
    }

    /**
     * 감정우표 선택
     */
    @Transactional
    public void updateEmotion(Long userId, HomeRequest.postEmotionDTO request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Emotion emotion = request.getEmotion();

        if(user.getTodayEmotion() == request.getEmotion()) throw new GeneralException(ErrorStatus.EMOTION_ALREADY_CHOSEN);

        user.setEmotion(emotion);

        // 오늘 중 이전에 생성된 맞춤형 질문 삭제
        questionService.deleteToYouQuestions(user);

        // 맞춤형 질문 생성
        List<CustomQuestion> customQuestions = customQuestionRepository.findByUserStatus(user.getStatus());

        // 필터링 조건에 따라 질문 생성
        createAndProcessRandomQuestion(user, customQuestions, user.getTodayEmotion(), QuestionType.LONG_ANSWER);
        createAndProcessRandomQuestion(user, customQuestions, null, QuestionType.SHORT_ANSWER);
        createAndProcessRandomQuestion(user, customQuestions, null, QuestionType.OPTIONAL);
    }

    // 랜덤 질문 생성 및 처리
    private void createAndProcessRandomQuestion(User user, List<CustomQuestion> customQuestions, Emotion emotion, QuestionType questionType) {
        List<CustomQuestion> filteredQuestions = customQuestions.stream()
                .filter(question -> (emotion == null || Objects.equals(question.getEmotion(), emotion)) && question.getQuestionType() == questionType)
                .collect(Collectors.toList());

        if (!filteredQuestions.isEmpty()) {
            CustomQuestion randomQuestion = getRandomQuestion(filteredQuestions);
            processQuestion(user, randomQuestion);
        }
    }

    // 랜덤 질문 추출
    private CustomQuestion getRandomQuestion(List<CustomQuestion> questions) {
        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());
        return questions.get(randomIndex);
    }

    // 질문 처리
    private void processQuestion(User user, CustomQuestion cq) {
        if (cq != null) {
            QuestionRequest.createQuestionDTO request = QuestionConverter.toCreateQuestionDTO(user, cq);
            questionService.autoCreateQuestion(request);
        }
    }

    /**
     * 감정 초기화(매일 자정 실행)
     */
    @Transactional
    public void resetTodayEmotion() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setEmotion(null);
        }
        userRepository.saveAll(users);
    }
}

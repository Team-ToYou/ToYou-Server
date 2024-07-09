package com.example.toyou.service.QuestionService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.converter.QuestionConverter;
import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.repository.AnswerOptionRepository;
import com.example.toyou.repository.QuestionRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    // 익명 이름 리스트
    private static final List<String> ANONYMOUS_NAMES = Arrays.asList(
            "심야의 족제비", "섬세한 독수리", "은밀한 여우", "산책하는 고양이", "졸린 판다",
            "자유로운 돌고래", "따분한 곰", "차분한 거북이", "우아한 백조", "소심한 토끼",
            "활발한 다람쥐", "용감한 강아지", "신비로운 올빼미", "산뜻한 오리", "겁많은 코끼리",
            "피곤한 사자", "말많은 원숭이", "달리는 치타", "화려한 플라밍고", "활기찬 호랑이"
    );

    @Transactional
    public void createQuestion(Long userId, QuestionRequest.createQuestionDTO request) {
        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String questioner = user.getNickname();

        // 질문 대상 검색
        User target = userRepository.findByNickname(request.getTarget())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if(user == target) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 익명 선택시
        if(request.isAnonymous()) {
            Random random = new Random();
            questioner = ANONYMOUS_NAMES.get(random.nextInt(ANONYMOUS_NAMES.size()));
        }

        QuestionType questionType = request.getQuestionType();

        Question newQuestion = QuestionConverter.toQuestion(target, questionType, questioner, request.getContent());

        questionRepository.save(newQuestion);

        // 선택형 일때만 답변 선택란 입력 가능
        if(questionType != QuestionType.OPTIONAL && request.getAnswerOptionList() != null) throw new GeneralException(ErrorStatus.INCORRECT_QUESTION_TYPE);

        // 선택형
        if(questionType == QuestionType.OPTIONAL) {

            if(request.getAnswerOptionList() == null) throw new GeneralException(ErrorStatus.EMPTIED_LIST);

            // answerOptionList를 AnswerOption 객체 리스트로 변환
            List<AnswerOption> answerOptions = QuestionConverter.toAnswerOptionList(request.getAnswerOptionList(), newQuestion);

            answerOptionRepository.saveAll(answerOptions);
        }
    }
}

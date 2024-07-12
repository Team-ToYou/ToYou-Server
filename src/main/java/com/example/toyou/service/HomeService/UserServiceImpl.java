package com.example.toyou.service.HomeService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.app.dto.HomeResponse;
import com.example.toyou.converter.UserConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.repository.AlarmRepository;
import com.example.toyou.repository.UserRepository;
import com.example.toyou.service.QuestionService.QuestionService;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

        user.setEmotion(emotion);

        // 맞춤형 질문 생성


//        questionService.createQuestion(user.getId(), );
    }
}

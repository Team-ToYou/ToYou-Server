package com.example.toyou.service.HomeService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.app.dto.HomeResponse;
import com.example.toyou.converter.UserConverter;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

        return UserConverter.toGetHomeDTO(user, cardId, questionNum);
    }

    /**
     * 감정우표 선택
     */
    @Transactional
    public void updateEmotion(Long userId, HomeRequest.postEmotionDTO request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Emotion emotion = request.getEmotion();

        user.setEmotion(emotion);
    }
}

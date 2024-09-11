package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.*;
import com.example.toyou.converter.QuestionConverter;
import com.example.toyou.converter.UserConverter;
import com.example.toyou.domain.*;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.domain.enums.Status;
import com.example.toyou.domain.mappings.UserCustomQuestion;
import com.example.toyou.repository.CustomQuestionRepository;
import com.example.toyou.repository.UserCustomQuestionRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.EXISTING_NICKNAME;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomQuestionRepository customQuestionRepository;
    private final UserCustomQuestionRepository userCustomQuestionRepository;
    private final QuestionService questionService;
    private final FriendService friendService;

    /**
     * 홈화면 조회
     */
    public HomeResponse.GetHomeDTO getHome(Long userId){

        // 유저 검색
        User user = findById(userId);

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
     * 닉네임 중복 확인
     */
    public UserResponse.checkUserNicknameDTO checkUserNickname(String nickname) {
        return UserResponse.checkUserNicknameDTO.builder()
                .exists(userRepository.existsByNickname(nickname))
                .build();
    }

    /**
     * 닉네임 수정
     */
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = findById(userId);

        if(userRepository.existsByNickname(nickname)) throw new GeneralException(EXISTING_NICKNAME);

        user.setNickname(nickname);
    }

    /**
     * 현재 상태 수정
     */
    @Transactional
    public void updateStatus(Long userId, Status status) {
        User user = findById(userId);
        user.setStatus(status);
    }

    /**
     * 마이페이지 조회
     */
    public UserResponse.GetMyPageDTO getMyPage(Long userId) {

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 리스트 조회
        List<User> friends = friendService.getFriendList(user);
        int friendNum = friends.size();

        return UserResponse.GetMyPageDTO.builder()
                .nickname(user.getNickname())
                .friendNum(friendNum)
                .status(user.getStatus())
                .build();
    }


    /**
     * 감정우표 선택
     */
    @Transactional
    public void postEmotion(Long userId, HomeRequest.postEmotionDTO request){

        User user = findById(userId);
        Emotion emotion = request.getEmotion();

        // 금일 감정을 이미 선택한 경우
        if(user.getTodayEmotion() != null) throw new GeneralException(ErrorStatus.EMOTION_ALREADY_CHOSEN);

        List<DiaryCard> diaryCards = user.getDiaryCardList();

        // 일기카드 생성시 감정 변경 불가능
        if (!diaryCards.isEmpty()) {
            DiaryCard latestCard = diaryCards.get(diaryCards.size() - 1); // 가장 최근 일기카드
            LocalDate today = LocalDate.now();
            if (latestCard.getCreatedAt().toLocalDate().equals(today)) {
                throw new GeneralException(ErrorStatus.DUPLICATE_CARD_FOR_TODAY);
            }
        }

        user.setEmotion(emotion);

        // 오늘 중 이전에 생성된 맞춤형 질문 삭제
//        questionService.deleteToYouQuestions(user);

        // 맞춤형 질문 생성
        List<CustomQuestion> customQuestions = customQuestionRepository.findByUserStatus(user.getStatus());

        // 필터링 조건에 따라 질문 생성
        createAndProcessRandomQuestion(user, customQuestions, user.getTodayEmotion(), QuestionType.LONG_ANSWER);
        createAndProcessRandomQuestion(user, customQuestions, null, QuestionType.SHORT_ANSWER);
        createAndProcessRandomQuestion(user, customQuestions, null, QuestionType.OPTIONAL);
    }

    // 랜덤 질문 생성 및 처리
    private void createAndProcessRandomQuestion(User user, List<CustomQuestion> customQuestions, Emotion emotion, QuestionType questionType) {
        // 이미 사용한 UserCustomQuestion 목록을 조회
        List<UserCustomQuestion> usedCustomQuestions = userCustomQuestionRepository.findByUserId(user.getId());

        // 사용된 CustomQuestion의 ID만 추출
        List<Long> usedQuestionIds = usedCustomQuestions.stream()
                .map(ucq -> ucq.getCustomQuestion().getId())
                .toList();

        List<CustomQuestion> filteredQuestions = customQuestions.stream()
                .filter(question -> (emotion == null || Objects.equals(question.getEmotion(), emotion))
                        && question.getQuestionType() == questionType
                        && !usedQuestionIds.contains(question.getId())) // 생성된 질문 제외
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

    // 질문 처리 및 자체 생성 내역 저장
    private void processQuestion(User user, CustomQuestion cq) {
        if (cq != null) {
            // 질문 생성
            QuestionRequest.createQuestionDTO request = QuestionConverter.toCreateQuestionDTO(user, cq);
            questionService.autoCreateQuestion(request);

            // 자체 생성 내역 저장
            UserCustomQuestion userCustomQuestion = UserCustomQuestion.builder()
                    .user(user)
                    .customQuestion(cq)
                    .build();

            userCustomQuestionRepository.save(userCustomQuestion);
        }
    }

    // 감정 초기화(매일 자정 실행)
    @Transactional
    public void resetTodayEmotion() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setEmotion(null);
        }
        userRepository.saveAll(users);
    }

    // id로 유저 검색
    public User findById(Long memberId) {
        return userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    // 닉네임으로 유저 검색
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }
}

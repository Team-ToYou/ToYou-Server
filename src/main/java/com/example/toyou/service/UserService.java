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
import com.example.toyou.repository.CustomQuestionRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.example.toyou.apiPayload.code.status.ErrorStatus.EXISTING_NICKNAME;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomQuestionRepository customQuestionRepository;
    private final QuestionService questionService;
    private final FriendService friendService;

    /**
     * 홈화면 조회
     */
    public HomeResponse.GetHomeDTO getHome(Long userId){

        log.info("[홈화면 조회] userId={}", userId);

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
        log.info("금일 생성한 일기카드 ID: {}", cardId);

        // 금일 받은 질문 갯수 조회
        int questionNum = 0;
        for (Question question : user.getQuestionList()) {
            if (today.equals(question.getCreatedAt().toLocalDate())) {
                questionNum++;
            }
        }
        log.info("금일 받은 질문 개수: {}", questionNum);

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
        log.info("[닉네임 중복 확인] nickname={}", nickname);

        return UserResponse.checkUserNicknameDTO.builder()
                .exists(userRepository.existsByNickname(nickname))
                .build();
    }

    /**
     * 닉네임 중복 확인(회원용)
     */
    public UserResponse.checkUserNicknameDTO checkUserNickname2(Long userId, String nickname) {
        log.info("[닉네임 중복 확인(회원용)] nickname={}, userId={}", nickname, userId);

        boolean isExist = false;

        if (userId != null) {
            User user = findById(userId);
            if(!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
                isExist = true;
            }
        } else {
            isExist = userRepository.existsByNickname(nickname);
        }

        return UserResponse.checkUserNicknameDTO.builder()
                .exists(isExist)
                .build();
    }

    /**
     * 닉네임 수정
     */
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        log.info("[닉네임 수정] userId={}, nickname={}", userId, nickname);

        User user = findById(userId);

        if(!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new GeneralException(EXISTING_NICKNAME);
        }

        user.setNickname(nickname);
    }

    /**
     * 현재 상태 수정
     */
    @Transactional
    public void updateStatus(Long userId, Status status) {
        log.info("[현재 상태 수정] userId={}, status={}", userId, status);

        User user = findById(userId);
        user.setStatus(status);
    }

    /**
     * 마이페이지 조회
     */
    public UserResponse.GetMyPageDTO getMyPage(Long userId) {
        log.info("[마이페이지 조회] userId={}", userId);

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 리스트 조회
        List<User> friends = friendService.getFriendList(user);
        int friendNum = friends.size();
        log.info("친구 수: {}", friendNum);

        return UserResponse.GetMyPageDTO.builder()
                .userId(userId)
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

        log.info("[감정우표 선택] userId={}, emotion={}", userId, request.getEmotion());

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

        // 맞춤형 질문 생성
        List<CustomQuestion> shorts = customQuestionRepository.findByUserStatusAndQuestionType(user.getStatus(), QuestionType.SHORT_ANSWER);
        List<CustomQuestion> optionals = customQuestionRepository.findByUserStatusAndQuestionType(user.getStatus(), QuestionType.OPTIONAL);

        // 필터링 조건에 따라 질문 생성
        createLongQuestion(user);
        createAndProcessRandomQuestion(user, shorts);
        createAndProcessRandomQuestion(user, optionals);
    }

    // 랜덤 질문 생성 및 처리(단문형 & 선택형)
    private void createAndProcessRandomQuestion(User user, List<CustomQuestion> questions) {

        // 랜덤 질문 추출
        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());
        CustomQuestion randomQuestion = questions.get(randomIndex);

        // 질문 생성
        if (randomQuestion != null) {
            QuestionRequest.createQuestionDTO request = QuestionConverter.toCreateQuestionDTO(user, randomQuestion);
            questionService.autoCreateQuestion(request);
        }
    }

    // 장문형 생성
    private void createLongQuestion(User user) {

        String content = switch (user.getTodayEmotion()) {
            case ANGRY -> "오늘 하루는 어떤 일이 있었길래 기분이 상했을까? 무슨 일이 있었는지 얘기해줄래?";
            case EXCITED -> "오늘 하루는 정말 흥미진진했겠네! 어떤 일이 너를 들뜨게 만들었을까?";
            case HAPPY -> "오늘 하루, 기분이 무척 좋았겠네! 어떤 일이 너를 행복하게 만들었어?";
            case NERVOUS -> "오늘 하루는 뭔가 불안했을 것 같은데, 무슨 일이 너를 초조하게 만들었어?";
            default -> "오늘 하루는 특별한 일은 없었지만, 그래도 평온한 기분이었을 것 같아. 뭐 하면서 보냈어?"; // NORMAL
        };

        CustomQuestion customQuestion = CustomQuestion.builder()
                .questionType(QuestionType.LONG_ANSWER)
                .content(content)
                .build();

        QuestionRequest.createQuestionDTO request = QuestionConverter.toCreateQuestionDTO(user, customQuestion);
        questionService.autoCreateQuestion(request);
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

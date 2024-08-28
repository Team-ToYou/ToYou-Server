package com.example.toyou.app;

import com.example.toyou.service.FcmService;
import com.example.toyou.service.QuestionService;
import com.example.toyou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private final QuestionService questionService;
    private final UserService userService;
    private final FcmService fcmService;

    /**
     * 매일 자정, 질문 데이터 초기화
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void cleanUpOldQuestions() {
        log.info("Starting cleanUpOldQuestions");
        questionService.deleteOldQuestions();
    }

    /**
     * 매일 자정, 감정 초기화
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetTodayEmotion() {
        log.info("Starting resetTodayEmotion");
        userService.resetTodayEmotion();
    }

    /**
     * 매일 자정, 60일 동안 사용되지 않은 FCM 토큰 삭제
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void cleanUpOldFcmTokens() {
        log.info("Starting cleanUpOldFcmTokens");
        fcmService.cleanUpOldFcmTokens();
    }
}

package com.example.toyou.global.job;

import com.example.toyou.service.FcmService;
import com.example.toyou.service.QuestionService;
import com.example.toyou.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
     * 매일 자정, 30일 동안 사용되지 않은 FCM 토큰 삭제
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void cleanUpOldFcmTokens() {
        log.info("Starting cleanUpOldFcmTokens");
        fcmService.cleanUpOldFcmTokens();
    }

    /**
     * 매일 23시 정기 알림
     */
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public void regularAlarm() throws IOException {
        log.info("Sending regularAlarm at 23:00");
        fcmService.sendMessageToTopic("allUsers", "일기카드 마감 1시간 전", "오늘의 일기카드가 곧 마감됩니다. 서두르세요!");
    }
}

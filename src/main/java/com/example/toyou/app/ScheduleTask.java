package com.example.toyou.app;

import com.example.toyou.service.UserService.UserService;
import com.example.toyou.service.QuestionService.QuestionService;
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

}

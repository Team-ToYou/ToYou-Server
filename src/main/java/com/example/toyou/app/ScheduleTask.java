package com.example.toyou.app;

import com.example.toyou.service.HomeService.UserService;
import com.example.toyou.service.QuestionService.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private final QuestionService questionService;
    private final UserService userService;

    /**
     * 매일 자정, 질문 데이터 초기화
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpOldQuestions() {
        questionService.deleteOldQuestions();
    }

    /**
     * 매일 자정, 감정 초기화
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetTodayEmotion() {
        userService.resetTodayEmotion();
    }

}

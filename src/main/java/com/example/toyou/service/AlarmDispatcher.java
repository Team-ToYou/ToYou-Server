package com.example.toyou.service;


import com.example.toyou.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmDispatcher {

    private final AlarmService alarmService;

    @Async("asyncExecutor")
    public void sendFriendAcceptedAlarm(User requestSender, User requestReceiver) {
        alarmService.createFriendRequestAcceptedAlarm(requestSender, requestReceiver);
    }
}

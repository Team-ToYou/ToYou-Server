package com.example.toyou.service;

import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.User;
import com.example.toyou.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmDispatcher {

    private final AlarmRepository alarmRepository;

    @Async("asyncExecutor")
    public void sendFriendAcceptedAlarm(User requestSender, User requestReceiver) {
        Alarm alarm = AlarmConverter.toFriendReqeustAcceptedAlarm(requestSender, requestReceiver);
        alarmRepository.save(alarm);
    }

    @Async("asyncExecutor")
    public void sendQuestionReceivedAlarm(String questioner, User target) {
        Alarm newAlarm = AlarmConverter.toNewQuestionAlarm(questioner, target);
        alarmRepository.save(newAlarm);
    }
}

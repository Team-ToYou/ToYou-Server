package com.example.toyou.event.listener;

import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.event.FriendRequestAcceptedEvent;
import com.example.toyou.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendRequestAcceptedListener {

    private final AlarmRepository alarmRepository;

    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(FriendRequestAcceptedEvent event) {
        try {
            Alarm alarm = AlarmConverter.toFriendReqeustAcceptedAlarm(
                    event.getSender(), event.getReceiver()
            );
            alarmRepository.save(alarm);
        } catch (Exception e) {
            log.error("알림 생성 실패 - message={}, thread={}", e.getMessage(), Thread.currentThread().getName(), e);
        }
    }
}

package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.AlarmResponse;
import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.User;
import com.example.toyou.repository.AlarmRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Transactional
    public AlarmResponse.getAlarmsDTO getAlarms(Long userId) {

        log.info("알림 목록 조회: userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Alarm> alarms = alarmRepository.findByUser(user);

        alarms.forEach(Alarm::setChecked);

        log.info("조회된 알림 개수 : {}", alarms.size());

        return AlarmConverter.toGetAlarmsDTO(alarms);
    }

    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteAlarm(Long userId, Long alarmId){

        log.info("알림 삭제: userId={}, alarmId={}", userId, alarmId);

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 알림 검색
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ALARM_NOT_FOUND));

        if(alarm.getUser() != user) throw new GeneralException(ErrorStatus.ALARM_NOT_MINE);

        alarmRepository.delete(alarm);
    }
}

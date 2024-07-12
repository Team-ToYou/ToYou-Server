package com.example.toyou.service.AlarmService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.AlarmResponse;
import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.converter.QuestionConverter;
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
public class AlarmServiceImpl implements AlarmService{

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Transactional
    public AlarmResponse.getAlarmsDTO getAlarms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Alarm> alarms = alarmRepository.findByUser(user);

        alarms.forEach(Alarm::setChecked);

        return AlarmConverter.toGetAlarmsDTO(alarms);
    }
}

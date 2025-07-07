package com.example.toyou.converter;

import com.example.toyou.dto.response.AlarmResponse;
import com.example.toyou.domain.*;
import com.example.toyou.domain.enums.AlarmType;

import java.util.List;

public class AlarmConverter {

    public static Alarm toFriendReqeustAcceptedAlarm(User user, User opponent) {

        return Alarm.builder()
                .user(user)
                .alarmType(AlarmType.FRIEND_REQUEST_ACCEPTED)
                .content(String.format("%s님이 친구 요청을 수락했습니다.", opponent.getNickname()))
                .opponent(opponent.getNickname())
                .checked(false)
                .build();
    }

    public static Alarm toNewQuestionAlarm(String questioner, User target) {

        return Alarm.builder()
                .user(target)
                .alarmType(AlarmType.NEW_QUESTION)
                .content(String.format("%s님이 질문카드를 보냈습니다. 확인해보세요!", questioner))
                .opponent(questioner)
                .checked(false)
                .build();
    }

    public static AlarmResponse.getAlarmsDTO toGetAlarmsDTO(List<Alarm> alarms) {

        List<AlarmResponse.alarmInfo> alarmInfos = alarms.stream()
                .map(alarm -> AlarmResponse.alarmInfo.builder()
                        .alarmId(alarm.getId())
                        .content(alarm.getContent())
                        .nickname(alarm.getOpponent())
                        .alarmType(alarm.getAlarmType())
                        .build())
                .toList();

        return AlarmResponse.getAlarmsDTO.builder().alarmList(alarmInfos).build();
    }
}

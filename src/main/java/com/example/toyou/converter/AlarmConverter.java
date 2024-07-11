package com.example.toyou.converter;

import com.example.toyou.domain.*;
import com.example.toyou.domain.enums.AlarmType;

public class AlarmConverter {
    public static Alarm toFriendReqeustAlarm(User user, User friend, FriendRequest friendRequest) {

        return Alarm.builder()
                .user(friend)
                .alarmType(AlarmType.FRIEND_REQUEST)
                .content(String.format("%s님이 친구 요청을 보냈습니다.", user.getNickname()))
                .checked(false)
                .friendRequest(friendRequest)
                .build();
    }

    public static Alarm toReqeustAcceptedAlarm(User user, User friend, FriendRequest friendRequest) {

        return Alarm.builder()
                .user(friend)
                .alarmType(AlarmType.REQUEST_ACCEPTED)
                .content(String.format("%s님이 친구 요청을 수락했습니다.", user.getNickname()))
                .checked(false)
                .friendRequest(friendRequest)
                .build();
    }

    public static Alarm toNewQuestionAlarm(User user, User target, Question question) {

        return Alarm.builder()
                .user(target)
                .alarmType(AlarmType.NEW_QUESTION)
                .content(String.format("%s님이 질문카드를 보냈습니다. 확인해보세요!", user.getNickname()))
                .checked(false)
                .question(question)
                .build();
    }
}

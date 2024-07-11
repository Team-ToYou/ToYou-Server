package com.example.toyou.converter;

import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
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
}

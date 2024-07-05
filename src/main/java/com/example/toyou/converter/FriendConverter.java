package com.example.toyou.converter;

import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;

import java.util.List;

public class FriendConverter {

    private static String getMessageForEmotion(Emotion emotion) {
        return switch (emotion) {
            case HAPPY -> "더없이 행복한 하루였어요";
            case EXCITED -> "들뜨고 흥분돼요";
            case NORMAL -> "평범한 하루였어요";
            case NERVOUS -> "생각이 많아지고 불안해요";
            case ANGRY -> "부글부글 화가 나요";
            default -> "";
        };
    }

    public static FriendResponse.GetFriendsDTO toGetFriendsDTO(List<User> friends){


        List<FriendResponse.friendInfo> friendInfoList = friends.stream()
                .map(friend -> {
                    Emotion todayEmotion = friend.getTodayEmotion();
                    String ment = (todayEmotion != null) ? getMessageForEmotion(todayEmotion) : null;

                    return FriendResponse.friendInfo.builder()
                            .nickname(friend.getNickname())
                            .emotion(todayEmotion)
                            .ment(ment)
                            .build();
                })
                .toList();

        return FriendResponse.GetFriendsDTO.builder().friendList(friendInfoList).build();
    }
}

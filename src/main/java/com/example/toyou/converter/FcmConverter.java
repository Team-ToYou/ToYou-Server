package com.example.toyou.converter;

import com.example.toyou.domain.User;
import com.example.toyou.app.dto.FcmRequest;

public class FcmConverter {

    public static FcmRequest.sendMessageDto toSendFcmDto(User user, String title, String body) {
        return FcmRequest.sendMessageDto.builder()
                .token(user.getFcmToken())
                .title(title)
                .body(body)
                .build();
    }
}

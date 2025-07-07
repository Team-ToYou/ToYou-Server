package com.example.toyou.event;

import com.example.toyou.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendRequestAcceptedEvent {

    private final User requestSender;
    private final User requestReceiver;
}

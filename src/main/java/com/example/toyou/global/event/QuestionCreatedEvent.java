package com.example.toyou.global.event;

import com.example.toyou.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionCreatedEvent {

    private final String questioner;
    private final User target;
}

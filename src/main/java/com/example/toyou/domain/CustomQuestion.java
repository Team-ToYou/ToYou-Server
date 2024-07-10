package com.example.toyou.domain;

import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import com.example.toyou.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private Status userStatus;

    private String content;
}

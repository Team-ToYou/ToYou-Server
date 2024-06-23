package com.example.toyou.domain;

import com.example.toyou.domain.common.BaseEntity;
import com.example.toyou.domain.constant.Emotion;
import com.example.toyou.domain.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Emotion todayEmotion;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Friend> friendList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DiaryCard> diaryCardList = new ArrayList<>();
}

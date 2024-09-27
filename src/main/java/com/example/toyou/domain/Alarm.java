package com.example.toyou.domain;

import com.example.toyou.domain.common.BaseEntity;
import com.example.toyou.domain.enums.AlarmType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String opponent;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_request_id")
    private FriendRequest friendRequest;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private Question question;

    public void setChecked(){
        this.checked = true;
    }

    public void updateForRequestAccepted(User user, User friend) {
        this.user = friend;
        this.alarmType = AlarmType.REQUEST_ACCEPTED;
        this.content = String.format("%s님이 친구 요청을 수락했습니다.", user.getNickname());
        this.opponent = user.getNickname();
        this.checked = false;
    }
}
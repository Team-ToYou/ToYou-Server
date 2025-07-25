package com.example.toyou.domain;

import com.example.toyou.domain.common.BaseEntity;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Embedded
    private OauthInfo oauthInfo;

    private boolean adConsent;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Emotion todayEmotion;

    private boolean isDeleted;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FriendRequest> friendRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DiaryCard> diaryCardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FcmToken> fcmTokenList = new ArrayList<>();

    public void setEmotion(Emotion emotion){
        this.todayEmotion = emotion;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void signup(String nickname, Status status, boolean adConsent){
        this.nickname = nickname;
        this.status = status;
        this.adConsent = adConsent;
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
        String randomSuffix = UUID.randomUUID().toString();
        this.nickname += randomSuffix;
        this.isDeleted = true;
    }
}

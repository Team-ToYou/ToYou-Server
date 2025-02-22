package com.example.toyou.domain;

import com.example.toyou.domain.common.BaseEntity;
import com.example.toyou.domain.enums.Emotion;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "diaryCard")
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    private boolean exposure;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    public void setExposure(boolean bool){
        this.exposure = bool;
    }

    public void toggleExposure(){
        this.exposure = !this.exposure;
    }
}

package com.example.toyou.domain;

import com.example.toyou.domain.common.BaseEntity;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private DiaryCard diaryCard;

    private String questioner;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    private String answer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AnswerOption> answerOptionList = new ArrayList<>();

    public void setDiaryCard(DiaryCard diaryCard){
        this.diaryCard = diaryCard;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }


    // ManyToOne 연관 관계 끊기
    public void deleteMappings() {
        // User와의 연관 관계 끊기
        if (this.user != null) {
            this.user.getQuestionList().remove(this);
            this.user = null;
        }

        // DiaryCard와의 연관 관계 끊기
        if (this.diaryCard != null) {
            this.diaryCard.getQuestionList().remove(this);
            this.diaryCard = null;
        }
    }
}

package com.example.toyou.converter;

import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.QuestionType;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static Question toQuestion(User user, QuestionType questionType, String questioner, String content, String answer) {
        return Question.builder()
                .user(user)
                .questionType(questionType)
                .questioner(questioner)
                .content(content)
                .answer(answer)
                .build();
    }

    public static AnswerOption toAnswerOption(String content, Question question) {
        return AnswerOption.builder()
                .content(content)
                .question(question)
                .selected(false)
                .build();
    }

    public static List<AnswerOption> toAnswerOptionList(List<String> answerOptionList, Question question) {
        return answerOptionList.stream()
                .map(content -> toAnswerOption(content, question))
                .collect(Collectors.toList());
    }
}

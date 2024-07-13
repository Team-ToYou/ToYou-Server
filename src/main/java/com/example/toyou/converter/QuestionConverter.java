package com.example.toyou.converter;

import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.app.dto.QuestionResponse;
import com.example.toyou.domain.AnswerOption;
import com.example.toyou.domain.CustomQuestion;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.Emotion;
import com.example.toyou.domain.enums.QuestionType;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static Question toQuestion(User user, QuestionType questionType, String questioner, String content) {
        return Question.builder()
                .user(user)
                .questionType(questionType)
                .questioner(questioner)
                .content(content)
                .build();
    }

    public static AnswerOption toAnswerOption(String content, Question question) {
        return AnswerOption.builder()
                .content(content)
                .question(question)
                .build();
    }

    public static List<AnswerOption> toAnswerOptionList(List<String> answerOptionList, Question question) {
        return answerOptionList.stream()
                .map(content -> toAnswerOption(content, question))
                .collect(Collectors.toList());
    }

    public static QuestionResponse.GetQuestionsDTO toGetQuestionDTO(List<Question> questions) {

        List<QuestionResponse.QuestionInfo> questionInfos = questions.stream()
                .map(question -> {
                    List<String> answerOptionContents = question.getAnswerOptionList().stream()
                            .map(AnswerOption::getContent)
                            .collect(Collectors.toList());

                    return QuestionResponse.QuestionInfo.builder()
                            .questionId(question.getId())
                            .content(question.getContent())
                            .questionType(question.getQuestionType())
                            .questioner(question.getQuestioner())
                            .answerOption(answerOptionContents)
                            .build();
                })
                .toList();

        return QuestionResponse.GetQuestionsDTO.builder().questionList(questionInfos).build();
    }

    public static QuestionRequest.createQuestionDTO toCreateQuestionDTO(User user, CustomQuestion cq) {
        return QuestionRequest.createQuestionDTO.builder()
                .target(user.getNickname())
                .content(cq.getContent())
                .questionType(cq.getQuestionType())
                .anonymous(false)
                .answerOptionList(cq.getAnswerOptions())
                .build();
    }
}

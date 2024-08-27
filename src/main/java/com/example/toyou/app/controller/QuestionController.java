package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.app.dto.QuestionResponse;
import com.example.toyou.service.QuestionService.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Question", description = "Question 관련 API입니다.")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * [POST] /questions
     * 질문 생성
     * @param userId 유저 식별자
     * @return
     */
    @PostMapping
    @Operation(summary = "질문 생성", description = "상대방에게 보낼 질문을 생성합니다.")
    public CustomApiResponse createQuestion(@RequestHeader Long userId,
                                           @RequestBody @Valid QuestionRequest.createQuestionDTO request) {

        questionService.createQuestion(userId, request);

        return CustomApiResponse.onSuccess(null);
    }

    /**
     * [GET] /questions
     * 질문 목록 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    @Operation(summary = "질문 목록 조회", description = "금일 받은 질문 목록을 조회합니다.")
    public CustomApiResponse<QuestionResponse.GetQuestionsDTO> getHome(@RequestHeader Long userId){

        QuestionResponse.GetQuestionsDTO questionList = questionService.getQuestions(userId);

        return CustomApiResponse.onSuccess(questionList);
    }

//    @DeleteMapping
//    public CustomApiResponse deleteOld(){
//
//        questionService.deleteOldQuestions();
//
//        return CustomApiResponse.onSuccess(null);
//    }
}

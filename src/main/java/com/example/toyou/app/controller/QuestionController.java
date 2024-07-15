package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.HomeResponse;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.app.dto.QuestionResponse;
import com.example.toyou.service.QuestionService.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Validated
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    /**
     * [POST] /questions
     * 질문 생성
     * @param userId 유저 식별자
     * @return
     */
    @PostMapping
    public ApiResponse createQuestion(@RequestHeader Long userId,
                                           @RequestBody @Valid QuestionRequest.createQuestionDTO request) {

        questionService.createQuestion(userId, request);

        return ApiResponse.onSuccess(null);
    }

    /**
     * [GET] /questions
     * 질문 목록 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    public ApiResponse<QuestionResponse.GetQuestionsDTO> getHome(@RequestHeader Long userId){

        QuestionResponse.GetQuestionsDTO questionList = questionService.getQuestions(userId);

        return ApiResponse.onSuccess(questionList);
    }

//    @DeleteMapping
//    public ApiResponse deleteOld(){
//
//        questionService.deleteOldQuestions();
//
//        return ApiResponse.onSuccess(null);
//    }
}

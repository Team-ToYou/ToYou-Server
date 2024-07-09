package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.QuestionRequest;
import com.example.toyou.service.QuestionService.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}

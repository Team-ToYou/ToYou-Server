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
     * [POST] /questions/optional
     * 질문 생성(선택형)
     * @param userId 유저 식별자
     * @return
     */
    @PostMapping("/optional")
    public ApiResponse createFriendRequest(@RequestHeader Long userId,
                                           @RequestBody @Valid QuestionRequest.createOptionalDTO request) {

        questionService.createOptional(userId, request);

        return ApiResponse.onSuccess(null);
    }
}

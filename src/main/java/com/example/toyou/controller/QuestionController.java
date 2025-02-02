package com.example.toyou.controller;

import com.example.toyou.common.apiPayload.CustomApiResponse;
import com.example.toyou.dto.response.FcmResponse;
import com.example.toyou.dto.request.QuestionRequest;
import com.example.toyou.dto.response.QuestionResponse;
import com.example.toyou.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Question", description = "Question 관련 API입니다.")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "질문 생성", description = "상대방에게 보낼 질문을 생성합니다.")
    public CustomApiResponse<FcmResponse.getMyNameDto> createQuestion(Principal principal,
                                                                      @RequestBody @Valid QuestionRequest.createQuestionDTO request) {

        Long userId = Long.parseLong(principal.getName());

        FcmResponse.getMyNameDto myName = questionService.createQuestion(userId, request);

        return CustomApiResponse.onSuccess(myName);
    }

    @GetMapping
    @Operation(summary = "질문 목록 조회", description = "금일 받은 질문 목록을 조회합니다.")
    public CustomApiResponse<QuestionResponse.GetQuestionsDTO> getHome(Principal principal){

        Long userId = Long.parseLong(principal.getName());

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

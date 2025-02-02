package com.example.toyou.common.apiPayload;

import com.example.toyou.common.apiPayload.code.BaseCode;
import com.example.toyou.common.apiPayload.code.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class CustomApiResponse<T> {

    @JsonProperty
    @Schema(description = "성공 여부", nullable = false, example = "true")
    private final Boolean isSuccess;
    @Schema(description = "상태 코드", nullable = false, example = "COMMON200")
    private final String code;
    @Schema(description = "상태 메세지", nullable = false, example = "성공입니다.")
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공한 경우 응답 생성
    public static <T> CustomApiResponse<T> onSuccess(T result){
        return new CustomApiResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), result);
        }

    public static <T> CustomApiResponse<T> of(BaseCode code, T result){
            return new CustomApiResponse<>(true, code.getReasonHttpStatus().getCode() , code.getReasonHttpStatus().getMessage(), result);
    }

    // 실패한 경우
    public static <T> CustomApiResponse<T> onFailure(String code, String message, T data) {
        return new CustomApiResponse<>(false, code, message, data);
    }
}

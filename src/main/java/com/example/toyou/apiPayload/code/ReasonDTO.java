package com.example.toyou.apiPayload.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "httpStatus", "code", "message"})
public class ReasonDTO {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

package com.example.toyou.apiPayload.code.status;

import com.example.toyou.apiPayload.code.BaseErrorCode;
import com.example.toyou.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반적인 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _ENUM_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, "COMMON404", "일치하는 타입이 없습니다"),

    // 유저 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER400", "해당하는 사용자가 존재하지 않습니다."),
    CANNOT_REQUEST_MYSELF(HttpStatus.BAD_REQUEST, "USER401", "스스로에게 요청할 수 없습니다."),
    EMOTION_ALREADY_CHOSEN(HttpStatus.NOT_FOUND, "USER40", "이미 오늘 감정으로 선택한 상태입니다."),

    // 친구 에러
    FRIEND_REQUEST_ALREADY_EXISTING(HttpStatus.BAD_REQUEST, "FRIEND400", "이미 친구 요청 정보가 존재합니다."),
    REQUEST_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "FRIEND401", "요청 정보가 존재하지 않습니다."),
    ALREADY_FRIENDS(HttpStatus.BAD_REQUEST, "FRIEND402", "이미 친구인 유저입니다."),

    // 질문 에러
    INCORRECT_QUESTION_TYPE(HttpStatus.BAD_REQUEST, "QUESTION400", "맞지 않는 질문 유형입니다."),
    EMPTIED_LIST(HttpStatus.BAD_REQUEST, "QUESTION401", "선택형 질문은 리스트가 비어있으면 안됩니다."),
    QUESTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "QUESTION402", "존재하지 않는 질문 ID입니다."),
    NOT_IN_OPTIONS(HttpStatus.BAD_REQUEST, "QUESTION403", "선택지에 없는 답안입니다."),

    // 카드 에러
    DUPLICATE_CARD_FOR_TODAY(HttpStatus.BAD_REQUEST, "CARD400", "금일 일기카드가 이미 생성되었습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "CARD401", "해당하는 일기카드가 존재하지 않습니다"),
    PRIVATE_CARD(HttpStatus.FORBIDDEN, "CARD402", "비공개 일기카드입니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, "CARD402", "일기카드 주인이 아닙니다."),

    // 알림 에러
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "ALARM400", "해당하는 알림이 존재하지 않습니다."),
    ALARM_NOT_MINE(HttpStatus.FORBIDDEN, "ALARM401", "해당 유저의 알림이 아닙니다."),

    // Oauth 에러
    OAUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "OAUTH400", "유효하지 않은 OAUTH 토큰입니다."),

    // JWT 에러
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "JWT400", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT401", "만료된 토큰입니다."),
    DIFFERENT_CATEGORY(HttpStatus.UNAUTHORIZED, "JWT402", "카테고리가 다른 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}

package com.example.toyou.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 애플 유저 정보 DTO
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Data
public class AppleUserInfoResponse {

    private String sub; // 고유 ID(==oauthId)
    private String email;
    private String refreshToken; // 애플 리프레시 토큰(회원탈퇴시 사용)
}
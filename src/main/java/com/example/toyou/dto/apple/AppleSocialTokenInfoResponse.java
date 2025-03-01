package com.example.toyou.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 애플 소셜 토큰 응답 정보 DTO
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class AppleSocialTokenInfoResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private String idToken;
}
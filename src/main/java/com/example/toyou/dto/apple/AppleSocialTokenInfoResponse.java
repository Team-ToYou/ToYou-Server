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

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;
}
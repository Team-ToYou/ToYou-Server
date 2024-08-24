package com.example.toyou.domain;


import com.example.toyou.domain.enums.OauthProvider;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {

    private String oauthId;

    @Enumerated(EnumType.STRING)
    private OauthProvider provider;

    private String oauthAccessToken;

    public void setOauthAccessToken(String oauthAccessToken){
        this.oauthAccessToken = oauthAccessToken;
    }
}

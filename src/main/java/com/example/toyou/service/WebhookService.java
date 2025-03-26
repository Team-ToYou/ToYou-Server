package com.example.toyou.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebhookService {

    // 디스코드 웹훅 URL 주입
    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    // 알림을 보내는 메서드
    public void sendDiscordNotification(String nickname, Long count) {

        // REST 요청을 처리하기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 알림 메시지 생성
        String message = count + "번째 회원이 등록되었습니다. [닉네임: " + nickname + "]";

        // HTTP 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 바디에 전송할 메시지 설정
        Map<String, String> body = new HashMap<>();
        body.put("content", message);

        // HTTP 요청 엔터티 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 디스코드 웹훅 URL로 POST 요청을 보내어 알림을 전송
        restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);
    }
}

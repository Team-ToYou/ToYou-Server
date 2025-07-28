package com.example.toyou.global.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NicknameUtils {

    private static final String[] ADJECTIVES = {
            "심야의", "섬세한", "은밀한", "산책하는", "졸린", "자유로운", "따분한",
            "차분한", "우아한", "소심한", "활발한", "용감한", "신비로운", "산뜻한",
            "겁많은", "피곤한", "말많은", "달리는", "화려한", "활기찬"
    };

    private static final String[] ANIMALS = {
            "족제비", "독수리", "여우", "고양이", "판다", "돌고래", "곰", "거북이",
            "백조", "토끼", "다람쥐", "강아지", "올빼미", "오리", "코끼리", "사자",
            "원숭이", "치타", "플라밍고", "호랑이"
    };

    private static final Random RANDOM = new Random();

    // 랜덤 닉네임 생성 메소드
    public String generateRandomNickname() {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String animal = ANIMALS[RANDOM.nextInt(ANIMALS.length)];
        return adjective + " " + animal;
    }
}

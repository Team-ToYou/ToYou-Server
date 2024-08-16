package com.example.toyou.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService { // Redis에 저장, 조회, 삭제하는 메서드를 구현하는 클래스.

    //RedisTemplate를 주입받아 Redis 데이터를 조작
    private final RedisTemplate<Long, String> redisTemplate;

    //key와 data를 Redis에 저장하는 메소드
    public void setValues(Long key, String data) {
        ValueOperations<Long, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    //key와 data를 Redis에 저장하고 만료 시간을 설정하는 메소드
    public void setValues(Long key, String data, Duration duration) {
        ValueOperations<Long, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
        log.info("key: " + key + " | value: " + data + " | duration: " + duration);
    }

    //key 파라미터로 받아 key를 기반으로 데이터를 조회하는 메소드
    @Transactional(readOnly = true)
    public String getValues(Long key) {
        ValueOperations<Long, String> values = redisTemplate.opsForValue();
        String result = values.get(key);
        return result != null ? result : "false"; // null일 경우 "false" 반환
    }

    //key를 파라미터로 받아 key를 기반으로 데이터를 삭제하는 메소드
    public void deleteValues(Long key) {
        redisTemplate.delete(key);
    }

    public void expireValues(Long key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }
}

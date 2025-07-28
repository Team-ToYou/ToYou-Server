package com.example.toyou.global.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheHelper {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T findWithCache(String key, TypeReference<T> typeRef, Supplier<T> dbFallback) {

        // 1. 캐시에서 먼저 조회
        String serializedCacheResult = redisTemplate.opsForValue().get(key);

        // 2. 캐시에 존재하면 반환.
        try {
            if (serializedCacheResult != null) {
                return objectMapper.readValue(serializedCacheResult, typeRef);
            }
        } catch (Exception e) {
            log.error("Cache read error for key {}: {}", key, e.getMessage());
        }

        // 3. DB 조회
        T result = dbFallback.get();

        // 4. DB 에서 찾은 경우, 캐시에 저장
        try {
            String serialized = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(key, serialized, Duration.ofMinutes(5));
        } catch (Exception e) {
            log.error("Cache write error for key {}: {}", key, e.getMessage());
        }

        return result;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteFriendCache(Long userId) {
        delete("friends:" + userId);
    }
}

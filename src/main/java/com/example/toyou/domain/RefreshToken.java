//package com.example.toyou.domain;
//
//import com.example.toyou.domain.common.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.redis.core.RedisHash;
//import org.springframework.data.redis.core.index.Indexed;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@AllArgsConstructor
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24) // 테스트용으로 60초
//public class RefreshToken extends BaseEntity {
//
//    @Id
//    private Long userId;
//
//    @Indexed
//    private String refreshToken;
//}

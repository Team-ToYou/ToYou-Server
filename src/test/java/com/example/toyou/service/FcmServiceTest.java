package com.example.toyou.service;

import com.example.toyou.common.apiPayload.exception.GeneralException;
import com.example.toyou.domain.FcmToken;
import com.example.toyou.domain.User;
import com.example.toyou.repository.FcmTokenRepository;
import com.example.toyou.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FcmServiceTest {

    @Autowired private FcmTokenRepository fcmTokenRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FcmService fcmService;

    @Test
    @DisplayName("유저의 FCM 토큰을 전부 삭제합니다.")
    void deleteAllToken() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        fcmService.saveToken(user.getId(), "fcm1");
        fcmService.saveToken(user.getId(), "fcm2");

        List<FcmToken> tokens = fcmTokenRepository.findAllByUser(user);
        assertEquals(2, tokens.size(), "저장된 토큰의 개수가 일치하지 않습니다.");

        // when
        fcmService.deleteAllToken(user);

        // then
        List<FcmToken> deletedTokens = fcmTokenRepository.findAllByUser(user);
        assertTrue(deletedTokens.isEmpty(), "토큰이 모두 삭제되지 않았습니다.");
    }

    @Test
    @DisplayName("FCM 토큰 갱신 성공")
    void updateToken() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        FcmToken fcmToken = FcmToken.builder()
                .user(user)
                .token("fcm")
                .connectedAt(LocalDateTime.of(2024, 10, 4, 10, 0))
                .build();

        fcmTokenRepository.save(fcmToken);

        // when
        fcmService.updateToken(user.getId(), "fcm");

        // then
        assertNotEquals(LocalDateTime.of(2024, 10, 4, 10, 0), fcmToken.getConnectedAt());
    }

    @Test
    @DisplayName("FCM 토큰 갱신 실패 - 존재하지 않는 토큰 정보")
    void updateToken_notFound() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        // when
        Exception e = assertThrows(GeneralException.class, () -> {
            fcmService.updateToken(user.getId(), "fcm");
        });

        assertThat(e.getMessage()).isEqualTo("FCM401: 해당 토큰 정보가 존재하지 않습니다.");
    }
}
package com.example.toyou.service;

import com.example.toyou.domain.FcmToken;
import com.example.toyou.domain.User;
import com.example.toyou.repository.FcmTokenRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FcmServiceTest {

    @Autowired private FcmTokenRepository fcmTokenRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FcmService fcmService;

    @Test
    @DisplayName("유저의 토큰을 전부 삭제합니다.")
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
}
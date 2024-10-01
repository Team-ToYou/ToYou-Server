package com.example.toyou.service;

import com.example.toyou.domain.DiaryCard;
import com.example.toyou.domain.User;
import com.example.toyou.repository.CardRepository;
import com.example.toyou.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CardServiceTest {

    @Autowired private CardRepository cardRepository;
    @Autowired private CardService cardService;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("일기카드 공개여부를 전환합니다.")
    void toggleExposure() {
        // given
        User user = User.builder().nickname("test").build();
        userRepository.save(user);

        DiaryCard card = DiaryCard.builder()
                .exposure(false)
                .user(user)
                .build();

        cardRepository.save(card);

        assertFalse(card.isExposure(), "일기카드 공개여부 설정 실패");

        // when
        cardService.toggleExposure(user.getId(), card.getId());

        // then
        assertTrue(card.isExposure(), "일기카드 공개여부 전환에 실패했습니다.");
    }
}
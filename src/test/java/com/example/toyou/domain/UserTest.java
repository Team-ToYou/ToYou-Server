package com.example.toyou.domain;
;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserTest {

    @Test
    @DisplayName("회원 탈퇴시 유저 닉네임 뒤에 랜덤 문자열을 추가한다.")
    void setDeletedAt() {
        // given
        User user = User.builder().nickname("test").build();

        // when
        user.setDeletedAt();

        // then
        String newNickname = user.getNickname();
        assertNotEquals("test", newNickname, "닉네임이 변경되지 않았습니다.");
    }
}
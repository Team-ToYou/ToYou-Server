package com.example.toyou.service;

import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FriendServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private FriendRepository friendRepository;
    @Autowired private FriendService friendService;

    @Test
    @DisplayName("친구 요청 목록 조회 - 성공")
    void getFriendRequestsTest() {
        // given
        User sender1 = User.builder().nickname("sender1").build();
        User sender2 = User.builder().nickname("sender2").build();
        User receiver = User.builder().nickname("receiver").build();

        userRepository.save(sender1);
        userRepository.save(sender2);
        userRepository.save(receiver);

        FriendRequest friendRequest1 = FriendRequest.builder()
                .user(sender1)
                .friend(receiver)
                .build();
        FriendRequest friendRequest2 = FriendRequest.builder()
                .user(sender2)
                .friend(receiver)
                .build();

        friendRepository.save(friendRequest1);
        friendRepository.save(friendRequest2);

        // when
        FriendResponse.getFriendRequestsDto friendRequests = friendService.getFriendRequests(receiver.getId());

        // then
        assertNotNull(friendRequests, "친구 요청 목록이 null입니다.");
        assertEquals(2, friendRequests.getSenderInfos().size(), "친구 요청 목록의 크기가 일치하지 않습니다.");

        List<FriendResponse.senderInfo> senderInfos = friendRequests.getSenderInfos();
        assertTrue(senderInfos.stream()
                .anyMatch(info -> info.getNickname().equals("sender1")), "sender1의 요청이 목록에 없습니다.");
        assertTrue(senderInfos.stream()
                .anyMatch(info -> info.getNickname().equals("sender2")), "sender2의 요청이 목록에 없습니다.");
    }

}

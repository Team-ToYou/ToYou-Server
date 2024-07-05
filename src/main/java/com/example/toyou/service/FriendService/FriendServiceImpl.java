package com.example.toyou.service.FriendService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.converter.FriendConverter;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 친구 목록 조회
     * @param userId 유저 식별자
     * @return
     */
    public FriendResponse.GetFriendsDTO getFriends(Long userId){

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests = friendRepository.findByUserAndAcceptedTrue(user);

        // 친구 리스트화
        List<User> friends = friendRequests.stream()
                .map(FriendRequest::getFriend)
                .toList();

        return FriendConverter.toGetFriendsDTO(friends);
    }

    /**
     * 친구(유저) 검색
     * @param keyword 검색어
     * @return
     */
    public FriendResponse.searchFriendDTO searchFriend(String keyword) {

        // 유저 검색
        User user = userRepository.findByNickname(keyword)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return FriendConverter.toSearchFriendDTO(user);
    }
}

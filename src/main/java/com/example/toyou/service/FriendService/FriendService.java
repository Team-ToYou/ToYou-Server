package com.example.toyou.service.FriendService;

import com.example.toyou.app.dto.FriendRequestDTO;
import com.example.toyou.app.dto.FriendResponse;

public interface FriendService {

    FriendResponse.GetFriendsDTO getFriends(Long userId);

    FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword);

    void createFriendRequest(Long userId, FriendRequestDTO.createFriendRequestDTO request);
}

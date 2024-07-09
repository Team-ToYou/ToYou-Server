package com.example.toyou.service.FriendService;

import com.example.toyou.app.dto.FriendRequest;
import com.example.toyou.app.dto.FriendResponse;

public interface FriendService {

    FriendResponse.GetFriendsDTO getFriends(Long userId);

    FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword);

    void createFriendRequest(Long userId, FriendRequest.createFriendRequestDTO request);

    void deleteFriendRequest(Long userId, FriendRequest.deleteFriendRequestDTO request);

    void acceptFriendRequest(Long userId, FriendRequest.acceptFriendRequestDTO request);
}

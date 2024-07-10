package com.example.toyou.service.FriendService;

import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;

public interface FriendService {

    FriendResponse.GetFriendsDTO getFriends(Long userId);

    FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword);

    void createFriendRequest(Long userId, FriendRequestRequest.createFriendRequestDTO request);

    void deleteFriendRequest(Long userId, FriendRequestRequest.deleteFriendRequestDTO request);

    void acceptFriendRequest(Long userId, FriendRequestRequest.acceptFriendRequestDTO request);

    FriendResponse.getFriendYesterdayDTO getFriendYesterday(Long userId);
}

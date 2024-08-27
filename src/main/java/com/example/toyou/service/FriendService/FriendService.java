package com.example.toyou.service.FriendService;

import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.domain.User;

import java.io.IOException;
import java.util.List;

public interface FriendService {

    FriendResponse.GetFriendsDTO getFriends(Long userId);

    FriendResponse.GetFriendNumDTO getFriendNum(Long userId);

    FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword);

    List<User> getFriendList(User user);

    FcmResponse.getMyNameDto createFriendRequest(Long userId, FriendRequestRequest.createFriendRequestDTO request);

    void deleteFriendRequest(Long userId, FriendRequestRequest.deleteFriendRequestDTO request);

    FcmResponse.getMyNameDto acceptFriendRequest(Long userId, FriendRequestRequest.acceptFriendRequestDTO request);

    FriendResponse.getFriendYesterdayDTO getFriendYesterday(Long userId);
}

package com.example.toyou.service.FriendService;

import com.example.toyou.app.dto.FriendResponse;

public interface FriendService {

    FriendResponse.GetFriendsDTO getFriends(Long userId);
}

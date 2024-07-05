package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.service.FriendService.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FriendController {

    private final FriendService friendService;

    /**
     * [GET] /friends
     * 친구 목록 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping
    public ApiResponse<FriendResponse.GetFriendsDTO> getFriends(@RequestHeader Long userId){

        FriendResponse.GetFriendsDTO friendList = friendService.getFriends(userId);

        return ApiResponse.onSuccess(friendList);
    }

    /**
     * [GET] /friends/search
     * 친구(유저) 검색
     * @param keyword 검색어
     * @return
     */
    @GetMapping("/search")
    public ApiResponse<FriendResponse.searchFriendDTO> searchFriend(@RequestParam(defaultValue = "") String keyword) {

        FriendResponse.searchFriendDTO friend = friendService.searchFriend(keyword);

        return ApiResponse.onSuccess(friend);
    }
}

package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.ApiResponse;
import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.service.FriendService.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
     * @param userId 유저 식별자
     * @param keyword 검색어
     * @return
     */
    @GetMapping("/search")
    public ApiResponse<FriendResponse.searchFriendDTO> searchFriend(@RequestHeader Long userId,
                                                                    @RequestParam(defaultValue = "") String keyword) {

        FriendResponse.searchFriendDTO friend = friendService.searchFriend(userId, keyword);

        return ApiResponse.onSuccess(friend);
    }

    /**
     * [POST] /friends
     * 친구 요청
     * @param userId 유저 식별자
     * @return
     */
    @PostMapping
    public ApiResponse createFriendRequest(@RequestHeader Long userId,
                                           @RequestBody @Valid FriendRequestRequest.createFriendRequestDTO request) {

        friendService.createFriendRequest(userId, request);

        return ApiResponse.onSuccess(null);
    }

    /**
     * [DELETE] /friends
     * 친구 요청 취소
     * @param userId 유저 식별자
     * @return
     */
    @DeleteMapping
    public ApiResponse deleteFriendRequest(@RequestHeader Long userId,
                                           @RequestBody @Valid FriendRequestRequest.deleteFriendRequestDTO request) {

        friendService.deleteFriendRequest(userId, request);

        return ApiResponse.onSuccess(null);
    }

    /**
     * [PATCH] /friends
     * 친구 요청 승인
     * @param userId 유저 식별자
     * @return
     */
    @PatchMapping
    public ApiResponse acceptFriendRequest(@RequestHeader Long userId,
                                           @RequestBody @Valid FriendRequestRequest.acceptFriendRequestDTO request) {

        friendService.acceptFriendRequest(userId, request);

        return ApiResponse.onSuccess(null);
    }

    /**
     * [GET] /friends/yesterday
     * 작일 친구 일기카드 목록 조회
     * @param userId 유저 식별자
     * @return
     */
    @GetMapping("/yesterday")
    public ApiResponse<FriendResponse.getFriendYesterdayDTO> searchFriend(@RequestHeader Long userId) {

        FriendResponse.getFriendYesterdayDTO friends = friendService.getFriendYesterday(userId);

        return ApiResponse.onSuccess(friends);
    }
}

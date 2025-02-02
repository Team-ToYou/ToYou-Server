package com.example.toyou.controller;

import com.example.toyou.common.apiPayload.CustomApiResponse;
import com.example.toyou.dto.response.FcmResponse;
import com.example.toyou.dto.request.FriendRequestRequest;
import com.example.toyou.dto.response.FriendResponse;
import com.example.toyou.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Friend", description = "Friend 관련 API입니다.")
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    @Operation(summary = "친구 목록 조회", description = "친구 목록을 조회합니다.")
    public CustomApiResponse<FriendResponse.GetFriendsDTO> getFriends(Principal principal){

        Long userId = Long.parseLong(principal.getName());

        FriendResponse.GetFriendsDTO friendList = friendService.getFriends(userId);

        return CustomApiResponse.onSuccess(friendList);
    }

    @GetMapping("/search")
    @Operation(summary = "친구(유저) 검색", description = "닉네임 검색을 통해 유저를 조회합니다.")
    public CustomApiResponse<FriendResponse.searchFriendDTO> searchFriend(Principal principal,
                                                                          @RequestParam(defaultValue = "") String keyword) {

        Long userId = Long.parseLong(principal.getName());

        FriendResponse.searchFriendDTO friend = friendService.searchFriend(userId, keyword);

        return CustomApiResponse.onSuccess(friend);
    }

    @GetMapping("/requests")
    @Operation(summary = "친구 요청 목록 조회", description = "나에게 친구 요청을 보낸 발신자 목록을 조회합니다.")
    public CustomApiResponse<FriendResponse.getFriendRequestsDto> getFriendRequests(Principal principal) {

        Long userId = Long.parseLong(principal.getName());

        FriendResponse.getFriendRequestsDto response = friendService.getFriendRequests(userId);

        return CustomApiResponse.onSuccess(response);
    }

    @PostMapping("/requests")
    @Operation(summary = "친구 요청", description = "다른 유저에게 친구 요청을 보냅니다.")
    public CustomApiResponse<FcmResponse.getMyNameDto> createFriendRequest(Principal principal,
                                                                           @RequestBody @Valid FriendRequestRequest.createFriendRequestDTO request) {

        Long userId = Long.parseLong(principal.getName());

        FcmResponse.getMyNameDto response = friendService.createFriendRequest(userId, request);

        return CustomApiResponse.onSuccess(response);
    }

    @DeleteMapping
    @Operation(summary = "친구 삭제, 요청 취소, 요청 거절", description = "친구 삭제, 친구 요청 취소, 친구 요청 거절시 사용합니다.")
    public CustomApiResponse<?> deleteFriendRequest(Principal principal,
                                                    @RequestBody @Valid FriendRequestRequest.deleteFriendRequestDTO request) {

        Long userId = Long.parseLong(principal.getName());

        friendService.deleteFriendRequest(userId, request);

        return CustomApiResponse.onSuccess(null);
    }

    @PatchMapping("/requests/approve")
    @Operation(summary = "친구 요청 승인", description = "상대방이 보낸 친구 요청을 승인합니다.")
    public CustomApiResponse<FcmResponse.getMyNameDto> acceptFriendRequest(Principal principal,
                                                                           @RequestBody @Valid FriendRequestRequest.acceptFriendRequestDTO request) {

        Long userId = Long.parseLong(principal.getName());

        FcmResponse.getMyNameDto response = friendService.acceptFriendRequest(userId, request);

        return CustomApiResponse.onSuccess(response);
    }
}
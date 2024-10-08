package com.example.toyou.app.controller;

import com.example.toyou.apiPayload.CustomApiResponse;
import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;
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

    @PostMapping("/requests")
    @Operation(summary = "친구 요청", description = "다른 유저에게 친구 요청을 보냅니다.")
    public CustomApiResponse<FcmResponse.getMyNameDto> createFriendRequest(Principal principal,
                                                              @RequestBody @Valid FriendRequestRequest.createFriendRequestDTO request) {

        Long userId = Long.parseLong(principal.getName());

        FcmResponse.getMyNameDto myName = friendService.createFriendRequest(userId, request);

        return CustomApiResponse.onSuccess(myName);
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

        FcmResponse.getMyNameDto myName = friendService.acceptFriendRequest(userId, request);

        return CustomApiResponse.onSuccess(myName);
    }

    @GetMapping("/yesterday")
    @Operation(summary = "작일 친구 일기카드 목록 조회", description = "어제 날짜 기준으로 생성된 친구들의 일기카드를 조회합니다.")
    public CustomApiResponse<FriendResponse.getFriendYesterdayDTO> searchFriend(Principal principal) {

        Long userId = Long.parseLong(principal.getName());

        FriendResponse.getFriendYesterdayDTO friends = friendService.getFriendYesterday(userId);

        return CustomApiResponse.onSuccess(friends);
    }
}

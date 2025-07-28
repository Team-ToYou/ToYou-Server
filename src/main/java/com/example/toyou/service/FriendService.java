package com.example.toyou.service;

import com.example.toyou.global.response.code.status.ErrorStatus;
import com.example.toyou.global.exception.GeneralException;
import com.example.toyou.global.cache.RedisCacheHelper;
import com.example.toyou.dto.response.FcmResponse;
import com.example.toyou.dto.request.FriendRequestRequest;
import com.example.toyou.dto.response.FriendResponse;
import com.example.toyou.converter.FriendConverter;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.FriendStatus;
import com.example.toyou.global.event.FriendRequestAcceptedEvent;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final RedisCacheHelper redisCacheHelper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 친구 목록 조회
     */
    public FriendResponse.GetFriendsDTO getFriends(Long userId) {

        log.info("[친구 목록 조회] userId={}", userId);
        String cacheKey = "friends:" + userId;

        return redisCacheHelper.findWithCache(
                cacheKey,
                new TypeReference<FriendResponse.GetFriendsDTO>() {},
                () -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

                    List<User> friends = getFriendList(user);
                    log.info("조회된 친구 수 : {}", friends.size());

                    return FriendConverter.toGetFriendsDTO(friends);
                }
        );
    }

    // 친구 리스트 조회
    public List<User> getFriendList(User user) {

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests1 = friendRepository.findBySenderAndAcceptedTrue(user);
        List<FriendRequest> friendRequests2 = friendRepository.findByReceiverAndAcceptedTrue(user);

        // 두 리스트 합치기
        return Stream.concat(
                        friendRequests1.stream().map(FriendRequest::getReceiver),
                        friendRequests2.stream().map(FriendRequest::getSender)
                )
                .distinct() // 중복 제거
                .toList();
    }

    /**
     * 친구(유저) 검색
     */
    public FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword) {

        log.info("[친구(유저) 검색] userId={}, keyword={}", userId, keyword);

        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findByNickname(keyword)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (user == friend) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 친구 상태 확인
        FriendStatus friendStatus = friendRepository.findBetween(user, friend)
                .map(fr -> {
                    if (fr.getAccepted()) return FriendStatus.FRIEND;
                    return fr.getSender().equals(user)
                            ? FriendStatus.REQUEST_SENT
                            : FriendStatus.REQUEST_RECEIVED;
                })
                .orElse(FriendStatus.NOT_FRIEND);

        log.info("친구 상태 : {}", friendStatus);

        return FriendConverter.toSearchFriendDTO(friend, friendStatus);
    }

    /**
     * 친구 요청 목록 조회
     */
    public FriendResponse.getFriendRequestsDto getFriendRequests(Long userId) {

        log.info("[친구 요청 목록 조회] userId={}", userId);

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 요청 목록 조회
        List<FriendRequest> friendRequests = friendRepository.findByReceiverAndAcceptedFalse(user);

        // 발신자 정보 조회
        List<FriendResponse.senderInfo> senderInfos = friendRequests.stream()
                .map(friendRequest -> {
                    User sender = friendRequest.getSender();

                    return FriendResponse.senderInfo.builder()
                            .userId(sender.getId())
                            .nickname(sender.getNickname())
                            .build();
                }).toList();


        log.info("조회된 친구 요청 목록 수 : {}", senderInfos.size());

        return FriendResponse.getFriendRequestsDto.builder()
                .senderInfos(senderInfos)
                .build();
    }

    /**
     * 친구 요청
     */
    @Transactional
    public FcmResponse.getMyNameDto createFriendRequest(Long userId, FriendRequestRequest.createFriendRequestDTO request) {

        log.info("[친구 요청] userId={}, friendId={}", userId, request.getUserId());

        // 본인 검색
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 상대방 검색
        User receiver = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 본인 스스로에게 친구 요청 불가능
        if (sender == receiver) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 친구 요청 정보가 이미 존재하는지 확인
        if (friendRepository.existsBySenderAndReceiver(sender, receiver) || friendRepository.existsBySenderAndReceiver(receiver, sender))
            throw new GeneralException(ErrorStatus.FRIEND_REQUEST_ALREADY_EXISTING);

        FriendRequest newFriendRequest = FriendConverter.toFriendRequest(sender, receiver);

        friendRepository.save(newFriendRequest);

        log.info("생성된 친구 요청 : friendRequestId={}", newFriendRequest.getId());

        return FcmResponse.getMyNameDto.builder()
                .myName(sender.getNickname())
                .build();
    }

    /**
     * 친구 삭제 & 친구 요청 취소
     */
    @Transactional
    public void deleteFriendRequest(Long userId, FriendRequestRequest.deleteFriendRequestDTO request) {

        log.info("[친구 삭제 & 친구 요청 취소] userId={}, friendId={}", userId, request.getUserId());

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 요청 정보 확인
        FriendRequest friendRequestToDelete = friendRepository.findBySenderAndReceiver(user, friend)
                .orElseGet(() ->
                        friendRepository.findBySenderAndReceiver(friend, user)
                                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_INFO_NOT_FOUND))
                );

        friendRepository.delete(friendRequestToDelete);

        // 캐시 무효화
        redisCacheHelper.deleteFriendCache(userId);
        redisCacheHelper.deleteFriendCache(friend.getId());
    }

    /**
     * 친구 요청 승인
     */
    @Transactional
    public FcmResponse.getMyNameDto acceptFriendRequest(Long userId, FriendRequestRequest.acceptFriendRequestDTO request) {

        log.info("[친구 요청 승인] userId={}, friendId={}", userId, request.getUserId());

        // 친구 신청 수신자(승인한 본인)
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 신청 발신자
        User sender = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 요청 정보 확인
        FriendRequest friendRequestToAccept = friendRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_INFO_NOT_FOUND));

        if (friendRequestToAccept.getAccepted()) throw new GeneralException(ErrorStatus.ALREADY_FRIENDS);

        friendRequestToAccept.setAccepted();

        // 캐시 무효화
        redisCacheHelper.deleteFriendCache(receiver.getId());
        redisCacheHelper.deleteFriendCache(sender.getId());

        // 알림 생성(친구 신청 발신자 대상)
        eventPublisher.publishEvent(new FriendRequestAcceptedEvent(sender, receiver));

        return FcmResponse.getMyNameDto.builder()
                .myName(receiver.getNickname())
                .build();
    }

    // 어제 일기카드를 생성한 친구들을 조회(작일 친구 일기카드 목록 조회시 사용)
    public List<User> getFriendsWithDiaryCardYesterday(Long userId) {
        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 오늘 날짜 기준 어제 날짜 계산
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // accepted가 true이고, 어제 생성된 DiaryCard가 있는 친구들의 친구 객체 리스트
        return Stream.concat(
                        // 본인이 sender인 경우
                        friendRepository.findBySenderAndAcceptedTrue(user).stream()
                                .map(FriendRequest::getReceiver)
                                .filter(friend -> !friend.isDeleted()) // isDeleted가 false인 경우만 포함
                                .filter(friend -> friend.getDiaryCardList().stream()
                                        .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday))),

                        // 본인이 receiver인 경우
                        friendRepository.findByReceiverAndAcceptedTrue(user).stream()
                                .map(FriendRequest::getSender)
                                .filter(friendRequestUser -> !friendRequestUser.isDeleted()) // isDeleted가 false인 경우만 포함
                                .filter(friendRequestUser -> friendRequestUser.getDiaryCardList().stream()
                                        .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday)))
                )
                .distinct() // 중복 제거
                .toList();
    }
}

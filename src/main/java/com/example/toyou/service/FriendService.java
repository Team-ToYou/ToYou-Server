package com.example.toyou.service;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.FcmResponse;
import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.converter.AlarmConverter;
import com.example.toyou.converter.FriendConverter;
import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.FriendStatus;
import com.example.toyou.repository.AlarmRepository;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    /**
     * 친구 목록 조회
     */
    public FriendResponse.GetFriendsDTO getFriends(Long userId) {

        log.info("[친구 목록 조회] userId={}", userId);

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 리스트 조회
        List<User> friends = getFriendList(user);

        log.info("조회된 친구 수 : {}", friends.size());

        return FriendConverter.toGetFriendsDTO(friends);
    }

    // 친구 리스트 조회
    public List<User> getFriendList(User user) {

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests1 = friendRepository.findByUserAndAcceptedTrue(user);
        List<FriendRequest> friendRequests2 = friendRepository.findByFriendAndAcceptedTrue(user);

        // 두 리스트 합치기
        return Stream.concat(
                        friendRequests1.stream().map(FriendRequest::getFriend),
                        friendRequests2.stream().map(FriendRequest::getUser)
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
        FriendStatus friendStatus;

        if (friendRepository.existsByUserAndFriendAndAccepted(user, friend, true)
                || friendRepository.existsByUserAndFriendAndAccepted(friend, user, true)) {
            friendStatus = FriendStatus.FRIEND;
        } else if (friendRepository.existsByUserAndFriendAndAccepted(user, friend, false)) {
            friendStatus = FriendStatus.REQUEST_SENT;
        } else if (friendRepository.existsByUserAndFriendAndAccepted(friend, user, false)) {
            friendStatus = FriendStatus.REQUEST_RECEIVED;
        } else {
            friendStatus = FriendStatus.NOT_FRIEND;
        }

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
        List<FriendRequest> friendRequests = friendRepository.findByFriendAndAcceptedFalse(user);

        // 발신자 정보 조회
        List<FriendResponse.senderInfo> senderInfos = friendRequests.stream()
                .map(friendRequest -> {
                    User sender = friendRequest.getUser();

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 본인 스스로에게 친구 요청 불가능
        if (user == friend) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 친구 요청 정보가 이미 존재하는지 확인
        if (friendRepository.existsByUserAndFriend(user, friend) || friendRepository.existsByUserAndFriend(friend, user))
            throw new GeneralException(ErrorStatus.FRIEND_REQUEST_ALREADY_EXISTING);

        FriendRequest newFriendRequest = FriendConverter.toFriendRequest(user, friend);

        friendRepository.save(newFriendRequest);

        log.info("생성된 친구 요청 : friendRequestId={}", newFriendRequest.getId());

        return FcmResponse.getMyNameDto.builder()
                .myName(user.getNickname())
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
        FriendRequest friendRequestToDelete = friendRepository.findByUserAndFriend(user, friend)
                .orElseGet(() ->
                        friendRepository.findByUserAndFriend(friend, user)
                                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_INFO_NOT_FOUND))
                );

        friendRepository.delete(friendRequestToDelete);
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
        FriendRequest friendRequestToAccept = friendRepository.findByUserAndFriend(sender, receiver)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_INFO_NOT_FOUND));

        if (friendRequestToAccept.getAccepted()) throw new GeneralException(ErrorStatus.ALREADY_FRIENDS);

        friendRequestToAccept.setAccepted();

        // 알림 생성(친구 신청 발신자 대상)
        Alarm newAlarm = AlarmConverter.toFriendReqeustAcceptedAlarm(receiver, sender);

        alarmRepository.save(newAlarm);

        log.info("생성된 알림 ID : {}", newAlarm.getId());

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
                        // User가 요청자인 경우
                        friendRepository.findByUserAndAcceptedTrue(user).stream()
                                .map(FriendRequest::getFriend)
                                .filter(friend -> !friend.isDeleted()) // isDeleted가 false인 경우만 포함
                                .filter(friend -> friend.getDiaryCardList().stream()
                                        .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday))),

                        // Friend가 요청자인 경우
                        friendRepository.findByFriendAndAcceptedTrue(user).stream()
                                .map(FriendRequest::getUser)
                                .filter(friendRequestUser -> !friendRequestUser.isDeleted()) // isDeleted가 false인 경우만 포함
                                .filter(friendRequestUser -> friendRequestUser.getDiaryCardList().stream()
                                        .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday)))
                )
                .distinct() // 중복 제거
                .toList();
    }
}

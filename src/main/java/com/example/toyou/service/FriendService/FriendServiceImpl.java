package com.example.toyou.service.FriendService;

import com.example.toyou.apiPayload.code.status.ErrorStatus;
import com.example.toyou.apiPayload.exception.GeneralException;
import com.example.toyou.app.dto.FriendRequestRequest;
import com.example.toyou.app.dto.FriendResponse;
import com.example.toyou.converter.FriendConverter;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import com.example.toyou.domain.enums.FriendStatus;
import com.example.toyou.repository.FriendRepository;
import com.example.toyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 친구 목록 조회
     */
    public FriendResponse.GetFriendsDTO getFriends(Long userId){

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // accepted가 true인 친구 요청 리스트 검색
        List<FriendRequest> friendRequests1 = friendRepository.findByUserAndAcceptedTrue(user);
        List<FriendRequest> friendRequests2 = friendRepository.findByFriendAndAcceptedTrue(user);

        // 두 리스트 합치기
        List<User> friends = Stream.concat(
                        friendRequests1.stream().map(FriendRequest::getFriend),
                        friendRequests2.stream().map(FriendRequest::getUser)
                )
                .distinct() // 중복 제거
                .collect(Collectors.toList());

        return FriendConverter.toGetFriendsDTO(friends);
    }

    /**
     * 친구(유저) 검색
     */
    public FriendResponse.searchFriendDTO searchFriend(Long userId, String keyword) {

        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findByNickname(keyword)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if(user == friend) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

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

        return FriendConverter.toSearchFriendDTO(friend, friendStatus);
    }

    /**
     * 친구 요청
     */
    @Transactional
    public void createFriendRequest(Long userId, FriendRequestRequest.createFriendRequestDTO request) {
        // 본인 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 본인 스스로에게 친구 요청 불가능
        if(user == friend) throw new GeneralException(ErrorStatus.CANNOT_REQUEST_MYSELF);

        // 친구 요청 정보가 이미 존재하는지 확인
        if(friendRepository.existsByUserAndFriend(user, friend) || friendRepository.existsByUserAndFriend(friend, user))
            throw new GeneralException(ErrorStatus.FRIEND_REQUEST_ALREADY_EXISTING);

        com.example.toyou.domain.FriendRequest newFriendRequest = FriendConverter.toFriendRequest(user, friend);

        friendRepository.save(newFriendRequest);
    }

    /**
     * 친구 삭제 & 친구 요청 취소
     */
    @Transactional
    public void deleteFriendRequest(Long userId, FriendRequestRequest.deleteFriendRequestDTO request){

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 검색
        User friend = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 요청 정보 확인
        com.example.toyou.domain.FriendRequest friendRequestToDelete = friendRepository.findByUserAndFriend(user, friend)
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
    public void acceptFriendRequest(Long userId, FriendRequestRequest.acceptFriendRequestDTO request){

        // 친구 신청 대상(본인)
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 신청 주체(상대)
        User requester = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 친구 요청 정보 확인
        FriendRequest friendRequestToAccept = friendRepository.findByUserAndFriend(requester, receiver)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REQUEST_INFO_NOT_FOUND));

        if(friendRequestToAccept.getAccepted()) throw new GeneralException(ErrorStatus.ALREADY_FRIENDS);

        friendRequestToAccept.setAccepted();
    }

    /**
     * 어제 일기카드를 생성한 친구 목록 조회
     */
    public FriendResponse.getFriendYesterdayDTO getFriendYesterday(Long userId){

        // 유저 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 오늘 날짜 기준 어제 날짜 계산
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // accepted가 true이고, 어제 생성된 DiaryCard가 있는 친구들의 닉네임 리스트
        List<String> friendNicknames1 = friendRepository.findByUserAndAcceptedTrue(user).stream()
                .filter(friendRequest -> {
                    User friend = friendRequest.getFriend();
                    return friend.getDiaryCardList().stream()
                            .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday));
                })
                .map(friendRequest -> friendRequest.getFriend().getNickname())
                .toList();

        List<String> friendNicknames2 = friendRepository.findByFriendAndAcceptedTrue(user).stream()
                .filter(friendRequest -> {
                    User friend = friendRequest.getUser();
                    return friend.getDiaryCardList().stream()
                            .anyMatch(diaryCard -> diaryCard.getCreatedAt().toLocalDate().isEqual(yesterday));
                })
                .map(friendRequest -> friendRequest.getUser().getNickname())
                .toList();

        // 두 리스트 합치기
        List<String> friendNicknames = Stream.concat(
                        friendNicknames1.stream(),
                        friendNicknames2.stream()
                )
                .distinct() // 중복 제거
                .toList();

        return FriendConverter.toYesterdayDTO(friendNicknames);
    }
}

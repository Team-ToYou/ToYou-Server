package com.example.toyou.repository;

import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendRequest, Long> {

    // 친구가 탈퇴되지 않았으며, 친구 요청이 수락된 경우에 대해 조회
    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.user = :user AND fr.accepted = true AND fr.friend.isDeleted = false")
    List<FriendRequest> findByUserAndAcceptedTrue(@Param("user") User user);

    // 친구가 탈퇴되지 않았으며, 수락된 친구 요청이 있는 경우에 대해 조회
    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.friend = :user AND fr.accepted = true AND fr.user.isDeleted = false")
    List<FriendRequest> findByFriendAndAcceptedTrue(@Param("user") User user);

    Optional<FriendRequest> findByUserAndFriend(User user, User friend);

    Boolean existsByUserAndFriend(User user, User friend);

    Boolean existsByUserAndFriendAndAccepted(User user, User friend, Boolean accepted);
}

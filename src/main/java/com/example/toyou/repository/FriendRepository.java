package com.example.toyou.repository;

import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByUserAndAcceptedTrue(User user);

    Boolean existsByUserAndFriend(User user, User friend);

    Boolean existsByUserAndFriendAndAccepted(User user, User friend, Boolean accepted);
}

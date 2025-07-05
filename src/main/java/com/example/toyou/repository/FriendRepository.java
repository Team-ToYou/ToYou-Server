package com.example.toyou.repository;

import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendRequest, Long> {

    @Query("""
        SELECT fr FROM FriendRequest fr
        JOIN FETCH fr.receiver
        WHERE fr.sender = :sender
          AND fr.accepted = true
          AND fr.receiver.isDeleted = false
    """)
    List<FriendRequest> findBySenderAndAcceptedTrue(@Param("sender") User sender);

    @Query("""
        SELECT fr FROM FriendRequest fr
        JOIN FETCH fr.sender
        WHERE fr.receiver = :sender
          AND fr.accepted = true
          AND fr.sender.isDeleted = false
    """)
    List<FriendRequest> findByReceiverAndAcceptedTrue(@Param("sender") User sender);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.receiver = :sender AND fr.accepted = false AND fr.sender.isDeleted = false")
    List<FriendRequest> findByReceiverAndAcceptedFalse(@Param("sender") User sender);


    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    Boolean existsBySenderAndReceiver(User sender, User receiver);

    Boolean existsBySenderAndReceiverAndAccepted(User sender, User receiver, Boolean accepted);
}

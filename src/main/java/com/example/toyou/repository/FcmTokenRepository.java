package com.example.toyou.repository;

import com.example.toyou.domain.FcmToken;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    List<FcmToken> findAllByUser(User user);

    Optional<FcmToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByConnectedAtBefore(LocalDateTime dateTime);
}

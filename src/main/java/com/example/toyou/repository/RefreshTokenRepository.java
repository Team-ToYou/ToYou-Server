package com.example.toyou.repository;

import com.example.toyou.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}

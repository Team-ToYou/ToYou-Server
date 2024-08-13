package com.example.toyou.repository;

import com.example.toyou.domain.OauthInfo;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByOauthInfo(OauthInfo oauthInfo);
}

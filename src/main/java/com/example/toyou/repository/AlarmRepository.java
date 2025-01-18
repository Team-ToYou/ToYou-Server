package com.example.toyou.repository;

import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.FriendRequest;
import com.example.toyou.domain.Question;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUser(User user);
}

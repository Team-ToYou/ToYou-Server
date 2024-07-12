package com.example.toyou.repository;

import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUser(User user);
}

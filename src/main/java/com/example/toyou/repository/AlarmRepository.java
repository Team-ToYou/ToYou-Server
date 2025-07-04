package com.example.toyou.repository;

import com.example.toyou.domain.Alarm;
import com.example.toyou.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUser(User user);

    @Query("SELECT COUNT(a) > 0 FROM Alarm a WHERE a.user = :user AND a.checked = false")
    boolean existsUncheckedAlarmByUser(@Param("user") User user);
}

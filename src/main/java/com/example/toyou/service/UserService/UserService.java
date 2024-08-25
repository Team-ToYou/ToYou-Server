package com.example.toyou.service.UserService;

import com.example.toyou.app.dto.HomeRequest;
import com.example.toyou.app.dto.HomeResponse;
import com.example.toyou.app.dto.UserResponse;
import com.example.toyou.domain.User;

public interface UserService {

    HomeResponse.GetHomeDTO getHome(Long userId);

    void updateEmotion(Long userId, HomeRequest.postEmotionDTO request);

    void resetTodayEmotion();

    User findById(Long userId);

    User findByNickname(String nickname);

    UserResponse.checkUserNicknameDTO checkUserNickname(String nickname);

    void updateNickname(Long userId, String nickname);
}

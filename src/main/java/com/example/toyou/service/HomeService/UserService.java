package com.example.toyou.service.HomeService;

import com.example.toyou.app.dto.HomeResponse;

public interface UserService {

    HomeResponse.GetHomeDTO getHome(Long userId);
}

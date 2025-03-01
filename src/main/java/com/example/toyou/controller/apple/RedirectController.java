package com.example.toyou.controller.apple;

import com.example.toyou.common.apiPayload.CustomApiResponse;
import com.example.toyou.dto.apple.AppleUserInfoResponse;
import com.example.toyou.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
public class RedirectController {

    private final AppleService appleService;

    @PostMapping("/apple")
    public CustomApiResponse<AppleUserInfoResponse> callback(HttpServletRequest request) throws Exception {
        AppleUserInfoResponse appleInfo = appleService.getAppleUserProfile(request.getParameter("code"));
        return CustomApiResponse.onSuccess(appleInfo);
    }
}

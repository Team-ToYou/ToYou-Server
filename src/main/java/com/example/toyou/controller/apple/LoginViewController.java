package com.example.toyou.controller.apple;

import com.example.toyou.service.AppleService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Controller
@Hidden
public class LoginViewController {

    private final AppleService appleService;

    @RequestMapping(value="/login/apple", method= RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("appleUrl", appleService.getAppleLogin());

        return "index";
    }
}

package com.lab1.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthWebController {
    @GetMapping("/signin")
    public String pageSignin() {
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String pageSignup() {
        return "auth/signup";
    }
}
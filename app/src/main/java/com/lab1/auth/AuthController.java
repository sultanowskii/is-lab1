package com.lab1.auth;

import org.springframework.web.bind.annotation.*;
import com.lab1.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;

    @PostMapping("/signup")
    public JwtAuthResponseDto signUp(@RequestBody @Valid SignUpRequestDto request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtAuthResponseDto signIn(@RequestBody @Valid SignInRequestDto request) {
        return authenticationService.signIn(request);
    }
}

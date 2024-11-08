package com.lab1.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab1.auth.dto.*;
import com.lab1.users.User;
import com.lab1.users.UserType;
import com.lab1.users.UserService;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public JwtAuthResponseDto signUp(SignUpRequestDto request) {
    var user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setType(UserType.USER);

    userService.create(user);

    var jwt = jwtService.generateToken(user);
    return new JwtAuthResponseDto(user, jwt);
  }

  @Transactional
  public JwtAuthResponseDto signIn(SignInRequestDto request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getUsername(),
        request.getPassword()
    ));

    var user = userService
      .userDetailsService()
      .loadUserByUsername(request.getUsername());

    var jwt = jwtService.generateToken(user);
    return new JwtAuthResponseDto(userService.getByUsername(user.getUsername()), jwt);
  }
}

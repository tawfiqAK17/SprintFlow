package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.exception.loginException.IncorrectPasswordException;
import com.ensa.SprintFlow.exception.loginException.UserNotVerifiedException;
import com.ensa.SprintFlow.exception.loginException.UsernameNotFoundException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.CustomUserDetails;
import com.ensa.SprintFlow.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  UserService userService;
  PasswordService passwordService;
  JwtService jwtService;

  LoginService(UserService userService, PasswordService passwordService, JwtService jwtService) {
    this.userService = userService;
    this.passwordService = passwordService;
    this.jwtService = jwtService;
  }

  public LoginResponseDto authenticateUser(LoginRequestDto dto) {
    User user = userService.findByUsername(dto.getUsername());
    if (user == null) {
      throw new UsernameNotFoundException();
    }
    if (!passwordService.match(dto.getPassword(), user.getPassword())) {
      throw new IncorrectPasswordException();
    }
    if (!user.isVerified()) {
      throw new UserNotVerifiedException();
    }
    String jwt = jwtService.generateToken(new CustomUserDetails(user));

    return new LoginResponseDto(jwt);
  }
}

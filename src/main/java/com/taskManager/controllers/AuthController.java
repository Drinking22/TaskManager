package com.taskManager.controllers;

import com.taskManager.dto.UserLoginDto;
import com.taskManager.dto.UserRegistrationDto;
import com.taskManager.entity.UserEntity;
import com.taskManager.services.auth.UserDetailsServiceImpl;
import com.taskManager.services.user.UserService;
import com.taskManager.utils.BaseLoggerService;
import com.taskManager.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthController extends BaseLoggerService {

    private UserService service;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserDetailsServiceImpl userDetailsService;

    public AuthController(UserService service, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserEntity> registerUser(@Validated @RequestBody UserRegistrationDto userDto) {
        logger.info("Registering user with e-mail: {}", userDto.getEmail());
        UserEntity registerUser = service.registerUser(userDto);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto userDto) {
        logger.info("Login user with e-mail: {}", userDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(jwt);
    }
}

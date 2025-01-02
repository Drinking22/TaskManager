package com.taskManager.controllers;

import com.taskManager.dto.UserRegistrationDto;
import com.taskManager.entity.UserEntity;
import com.taskManager.services.user.UserService;
import com.taskManager.utils.BaseLoggerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthController extends BaseLoggerService {

    private UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserEntity> registerUser(@Validated @RequestBody UserRegistrationDto userDto) {
        logger.info("Registering user with e-mail: {}", userDto.getEmail());
        UserEntity registerUser = service.registerUser(userDto);
        return ResponseEntity.ok(registerUser);
    }
}

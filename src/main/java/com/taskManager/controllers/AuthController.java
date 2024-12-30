package com.taskManager.controllers;

import com.taskManager.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class AuthController {

    @GetMapping("/{id}")
    public UserEntity getUser(@RequestParam Long id) {

        return null;
    }

    @GetMapping()
    public Collection<UserEntity> getAllUsers() {

        return java.util.List.of();
    }


}

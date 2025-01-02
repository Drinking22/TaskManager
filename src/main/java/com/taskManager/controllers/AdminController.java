package com.taskManager.controllers;

import com.taskManager.entity.UserEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class AdminController {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity getUser(@RequestParam Long id) {

        return null;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Collection<UserEntity> getAllUsers() {

        return java.util.List.of();
    }
}

package com.taskManager.services;

import com.taskManager.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity getUserByEmail(String email);
    List<UserEntity> getAllUsers();
    UserEntity getUserById(Long id);
}

package com.taskManager.services.admin;

import com.taskManager.entity.UserEntity;

import java.util.List;

public interface AdminService {
    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
    UserEntity createUser(UserEntity user);
    UserEntity updateUSer(UserEntity user);
    void deleteUserById(Long id);
}

package com.taskManager.services.admin;

import com.taskManager.entity.UserEntity;
import com.taskManager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private UserRepository repository;

    public AdminServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return null;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return List.of();
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        return null;
    }

    @Override
    public UserEntity updateUSer(UserEntity user) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}

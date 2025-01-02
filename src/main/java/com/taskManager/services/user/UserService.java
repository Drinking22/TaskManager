package com.taskManager.services.user;

import com.taskManager.dto.UserRegistrationDto;
import com.taskManager.entity.UserEntity;

public interface UserService {
    UserEntity registerUser(UserRegistrationDto userDto);
}

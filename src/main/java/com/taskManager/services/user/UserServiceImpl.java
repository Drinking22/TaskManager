package com.taskManager.services.user;

import com.taskManager.dto.UserRegistrationDto;
import com.taskManager.entity.UserEntity;
import com.taskManager.exceptions.UserAlreadyExistsException;
import com.taskManager.repository.UserRepository;
import com.taskManager.utils.BaseLoggerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseLoggerService implements UserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity registerUser(UserRegistrationDto userDto) {
        logger.info("Save user with e-mail: {}", userDto.getEmail());

        if (repository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.info("Attempt to register user with existing email {}", userDto.getEmail());
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        UserEntity saveUser = createUser(userDto);
        return repository.save(saveUser);
    }

    private UserEntity createUser(UserRegistrationDto userDto) {
        logger.info("Create user entity");
        UserEntity saveUser =  new UserEntity();
        saveUser.setFirstName(userDto.getFirstName());
        saveUser.setLastName(userDto.getLastName());
        saveUser.setEmail(userDto.getEmail());
        saveUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        saveUser.setRole(userDto.getRole());
        return saveUser;
    }
}

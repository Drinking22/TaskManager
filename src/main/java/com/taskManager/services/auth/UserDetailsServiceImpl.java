package com.taskManager.services.auth;

import com.taskManager.entity.UserEntity;
import com.taskManager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Реализация сервиса для загрузки данных пользователя по имени пользователя (в данном случае - по email).
 * Этот класс использует репозиторий пользователей для получения информации о пользователе.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Загружает пользователя по указанному email.
     *
     * @param email - адрес электронной почты пользователя
     * @return UserDetails - объект, содержащий информацию о пользователе
     * @throws UsernameNotFoundException если пользователь с указанным email не найден
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User with e-mail: " + email + " not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

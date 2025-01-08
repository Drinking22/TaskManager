package com.taskManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskManager.dto.UserLoginDto;
import com.taskManager.dto.UserRegistrationDto;
import com.taskManager.entity.UserEntity;
import com.taskManager.exceptions.UserAlreadyExistsException;
import com.taskManager.services.auth.UserDetailsServiceImpl;
import com.taskManager.services.user.UserServiceImpl;
import com.taskManager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    private ObjectMapper mapper;
    UserLoginDto userLoginDto;
    UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        userLoginDto = new UserLoginDto("example@mail.ru", "example_password");
        registrationDto = new UserRegistrationDto("John", "Doe", "example@mail.ru",
                "example_password", "USER");
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenGetRegisterDtoInRegisterUser_thanReturnUser() throws Exception {
        UserEntity user = new UserEntity(1L, "John", "Doe", "example@mail.ru",
                "example_password", null, "USER");

        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/registration")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registrationDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.first_name").value(user.getFirstName()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenGetRegisterUserInRegisterUser_thanThrowException() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new UserAlreadyExistsException("User with this email already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registrationDto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenLoginUser_thanReturnJwt() throws Exception {
        UserDetails userDetails = mock(UserDetails.class);
        String jwt = "jwt_test_token";
        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(userDetailsService.loadUserByUsername(userLoginDto.getEmail()))
                .thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(userLoginDto.getEmail());

        when(jwtUtil.generateToken(userLoginDto.getEmail()))
                .thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userLoginDto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(jwt));
    }

    @Test
    @WithMockUser(roles = "USER")
    void whenLoginUser_thanReturnUnauthorized() throws Exception {
        Authentication authentication = mock(Authentication.class);

        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userLoginDto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
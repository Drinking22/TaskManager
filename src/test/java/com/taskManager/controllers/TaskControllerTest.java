package com.taskManager.controllers;

import com.taskManager.entity.TaskEntity;
import com.taskManager.entity.UserEntity;
import com.taskManager.entity.UserRole;
import com.taskManager.exceptions.TaskNotFoundException;
import com.taskManager.services.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    private static final Long ID = 1L;

    @Autowired
    private MockMvc mock;

    @MockitoBean
    private TaskServiceImpl service;

    private UserEntity user;
    private TaskEntity task;

    @BeforeEach
    void setUp() {
        user = new UserEntity(
                1L, "John", "Doe", "example@test.com", "examplePassword", null, UserRole.ADMIN);
        task = new TaskEntity(1L, user, "Test task", "Test description");
    }

    @Test
    void whenGetTaskId_thenReturnTask() throws Exception {
        when(service.getTaskById(ID)).thenReturn(task);

        mock.perform(MockMvcRequestBuilders.get("/task/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    void whenGetTaskById_thenThrowException() throws Exception {
        when(service.getTaskById(ID)).thenThrow(new TaskNotFoundException("Task with id: " + ID + " not found"));

        mock.perform(MockMvcRequestBuilders.get("/task/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task with id: " + ID + " not found"));
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void createTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }
}
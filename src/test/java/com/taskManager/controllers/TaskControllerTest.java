package com.taskManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskManager.entity.TaskEntity;
import com.taskManager.entity.UserEntity;
import com.taskManager.exceptions.TaskNotFoundException;
import com.taskManager.services.task.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    private static final Long ID = 1L;

    @Autowired
    private MockMvc mock;

    @MockitoBean
    private TaskServiceImpl service;

    private ObjectMapper mapper;
    private UserEntity user;
    private TaskEntity task;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        user = new UserEntity(
                1L, "John", "Doe", "example@test.com", "examplePassword", null, "ADMIN");
        task = new TaskEntity(1L, user, "Test task", "Test description");
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void whenGetTaskId_thenReturnTask() throws Exception {
        when(service.getTaskById(ID)).thenReturn(task);

        mock.perform(MockMvcRequestBuilders.get("/task/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void whenGetTaskById_thenThrowException() throws Exception {
        when(service.getTaskById(ID)).thenThrow(new TaskNotFoundException("Task with id: " + ID + " not found"));

        mock.perform(MockMvcRequestBuilders.get("/task/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task with id: " + ID + " not found"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void whenGetAllTasks_thenReturnListOfTasks() throws Exception {
        List<TaskEntity> taskList = List.of(
                new TaskEntity(1L, user, "Test title id 1", "Test description id 1"),
                new TaskEntity(2L, user, "Test title id 2", "Test description id 2")
        );

        when(service.getAllTasks()).thenReturn(taskList);

        mock.perform(MockMvcRequestBuilders.get("/task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test title id 1")))
                .andExpect(jsonPath("$[0].description", is("Test description id 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test title id 2")))
                .andExpect(jsonPath("$[1].description", is("Test description id 2")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenCreateTask_thenReturnNewTask() throws Exception {
        TaskEntity newTask =
                new TaskEntity(ID, user, "New test title", "New test description");

        when(service.createTask(any(TaskEntity.class))).thenReturn(newTask);

        mock.perform(MockMvcRequestBuilders.post("/task")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newTask)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newTask.getId()))
                .andExpect(jsonPath("$.title").value(newTask.getTitle()))
                .andExpect(jsonPath("$.description").value(newTask.getDescription()))
                .andExpect(jsonPath("$.user_id.id").value(newTask.getUser().getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenUpdateTask_thenReturnUpdatedTask() throws Exception {
        TaskEntity updateTask =
                new TaskEntity(ID, user, "Update title", "Update description");

        when(service.updateTask(any(TaskEntity.class))).thenReturn(updateTask);

        mock.perform(MockMvcRequestBuilders.put("/task")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateTask)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updateTask.getId()))
                .andExpect(jsonPath("$.title").value(updateTask.getTitle()))
                .andExpect(jsonPath("$.description").value(updateTask.getDescription()))
                .andExpect(jsonPath("$.user_id.id").value(updateTask.getUser().getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void whenDeleteTask_thenGetStatusNoContent() throws Exception {
        doNothing().when(service).deleteTaskById(ID);

        mock.perform(MockMvcRequestBuilders.delete("/task/{id}", ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
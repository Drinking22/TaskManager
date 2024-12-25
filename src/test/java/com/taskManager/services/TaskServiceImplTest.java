package com.taskManager.services;

import com.taskManager.entity.TaskEntity;
import com.taskManager.entity.UserEntity;
import com.taskManager.entity.UserRole;
import com.taskManager.exceptions.TaskNotFoundException;
import com.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    private static final Long ID = 1L;

    @Mock
    private TaskRepository repository;

    @InjectMocks
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
    void whenGetTaskById_thenReturnTask() {
        when(repository.findById(ID)).thenReturn(Optional.ofNullable(task));

        TaskEntity testTask = service.getTaskById(ID);

        assertEquals(ID, testTask.getId());
        assertEquals("Test task", testTask.getTitle());
        assertEquals("Test description", testTask.getDescription());
    }

    @Test
    void whenTaskById_thenThrowException() {
        when(repository.findById(ID)).thenThrow(new TaskNotFoundException("Task with id: " + ID + " not found"));

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            service.getTaskById(ID);
        });

        assertEquals("Task with id: " + ID + " not found", exception.getMessage());
    }

    @Test
    void whenGetAllTasks_thenReturnListOfTasks() {
        when(repository.findAll()).thenReturn(List.of(task, task));

        List<TaskEntity> testList = service.getAllTasks();

        assertEquals(2, testList.size());
    }

    @Test
    void whenCreateTask_thenReturnTask() {
        when(repository.save(task)).thenReturn(task);

        TaskEntity testTask = service.createTask(task);

        assertEquals(task, testTask);
    }

    @Test
    void whenUpdateTask_thenReturnUpdatedTask() {
        when(repository.findById(ID)).thenReturn(Optional.of(task));
        when(repository.save(any(TaskEntity.class))).thenReturn(task);

        task.setTitle("Update title");
        task.setDescription("Update description");
        TaskEntity updateTask = service.updateTask(task);

        assertEquals("Update title", updateTask.getTitle());
        assertEquals("Update description", updateTask.getDescription());
        assertEquals(user, updateTask.getUser());
        verify(repository).save(task);
    }

    @Test
    void whenTaskNotExist_thenCreateNewTask() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        when(repository.save(any(TaskEntity.class))).thenReturn(task);

        TaskEntity createTask = service.updateTask(task);

        assertEquals("Test task", createTask.getTitle());
        assertEquals("Test description", createTask.getDescription());
        assertEquals(user, createTask.getUser());
        verify(repository).save(task);
    }

    @Test
    void whenDeleteExistingTask_thenTaskShouldBeDeleted() {
        when(repository.findById(ID)).thenReturn(Optional.of(task));

        service.deleteTaskById(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void whenTaskNotExist_thenThrowException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            service.deleteTaskById(ID);
        });

        assertEquals("Task with id: " + ID + " not found", exception.getMessage());
    }
}
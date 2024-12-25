package com.taskManager.services;

import com.taskManager.entity.TaskEntity;

import java.util.List;

public interface TaskService {
    TaskEntity getTaskById(Long id);
    List<TaskEntity> getAllTasks();
    TaskEntity createTask(TaskEntity task);
    TaskEntity updateTask(TaskEntity task);
    void deleteTaskById(Long id);
}

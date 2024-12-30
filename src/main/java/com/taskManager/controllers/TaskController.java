package com.taskManager.controllers;

import com.taskManager.entity.TaskEntity;
import com.taskManager.services.TaskService;
import com.taskManager.utils.BaseLoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController extends BaseLoggerService {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TaskEntity getTask(@PathVariable Long id) {
        logger.info("Try to get task with id: {}", id);
        return service.getTaskById(id);
    }

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        logger.info("Get all tasks");
        return service.getAllTasks();
    }

    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity task) {
        logger.info("Create new task");
        TaskEntity newTask = service.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskEntity task) {
        logger.info("Update task with id: {}", task.getId());
        TaskEntity updateTask = service.updateTask(task);
        return new ResponseEntity<>(updateTask, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        logger.info("Try to delete task with id: {}", id);
        service.deleteTaskById(id);
    }
}

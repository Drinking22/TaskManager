package com.taskManager.controllers;

import com.taskManager.entity.TaskEntity;
import com.taskManager.services.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TaskEntity getTask(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @GetMapping
    public TaskEntity getAllTasks() {
        return null;
    }

    @PostMapping
    public TaskEntity createTask(@RequestBody TaskEntity task) {

        return null;
    }

    @PutMapping
    public TaskEntity updateTask(@RequestBody TaskEntity task) {

        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@RequestParam Long id) {

    }
}

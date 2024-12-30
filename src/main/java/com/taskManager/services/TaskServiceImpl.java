package com.taskManager.services;

import com.taskManager.utils.BaseLoggerService;
import com.taskManager.entity.TaskEntity;
import com.taskManager.exceptions.TaskNotFoundException;
import com.taskManager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl extends BaseLoggerService implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public TaskEntity getTaskById(Long id) {
        logger.info("Get task by id: {}", id);
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found"));
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        logger.info("Get all tasks");
        return repository.findAll();
    }

    @Override
    public TaskEntity createTask(TaskEntity task) {
        logger.info("Create task with id: {}", task.getId());
        return repository.save(task);
    }

    @Override
    public TaskEntity updateTask(TaskEntity task) {
        Optional<TaskEntity> optionalExistingTask = repository.findById(task.getId());

        if (optionalExistingTask.isPresent()) {
            TaskEntity updateTask = optionalExistingTask.get();

            updateTask.setUser(task.getUser());
            updateTask.setTitle(task.getTitle());
            updateTask.setDescription(task.getDescription());

            logger.info("Update task with id: {}", updateTask.getId());
            return repository.save(updateTask);
        } else {
            logger.info("Task with id: {} not found, creating new task", task.getId());
            return createTask(task);
        }
    }

    @Override
    public void deleteTaskById(Long id) {
        Optional<TaskEntity> optionalDeleteTask = repository.findById(id);

        if (optionalDeleteTask.isPresent()) {
            logger.info("Delete task with id: {}", id);
            repository.deleteById(id);
        } else {
            logger.error("Task with id: {} not found for deletion", id);
            throw new TaskNotFoundException("Task with id: " + id + " not found");
        }
    }
}

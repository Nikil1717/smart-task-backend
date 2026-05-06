package com.smart.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.TaskRequestDTO;
import com.smart.dto.TaskResponseDTO;
import com.smart.entity.Task;
import com.smart.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO task) {
        return taskService.createTask(task, 1L); // hardcoded user
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponseDTO> getByProject(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId, 1L);
    }

    @GetMapping("/user/{userId}")
    public List<TaskResponseDTO> getByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId, 1L);
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponseDTO updateStatus(@PathVariable Long taskId, @RequestParam String status) {
        return taskService.updateTaskStatus(taskId, status, 1L);
    }
}

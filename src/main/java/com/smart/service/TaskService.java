package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smart.entity.Project;
import com.smart.entity.Task;
import com.smart.entity.User;
import com.smart.repository.ProjectRepository;
import com.smart.repository.TaskRepository;
import com.smart.repository.UserRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;

	public TaskService(TaskRepository taskRepository, UserRepository userRepository,
			ProjectRepository projectRepository) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
	}

	public Task createTask(Task task, Long currentUserId) {
		Project project = projectRepository.findById(task.getProject().getId())
				.orElseThrow(() -> new RuntimeException("Project Not Found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new RuntimeException("Unathorized");
		}

		User assignedUser = userRepository.findById(task.getAssignedUser().getId())
				.orElseThrow(() -> new RuntimeException("Assiggned User Not Found"));

		if (!assignedUser.getId().equals(project.getUser().getId())) {
		    throw new RuntimeException("Assigned user must belong to project");
		}
		
		task.setProject(project);
		task.setAssignedUser(assignedUser);

		return taskRepository.save(task);
	}

	public List<Task> getTasksByProject(Long projectId, Long currentUserId) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new RuntimeException("Unauthorized");
		}
		return taskRepository.findByProjectId(projectId);

	}

	public List<Task> getTasksByUser(Long userId, Long currentUserId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		if (!userId.equals(currentUserId)) {
			throw new RuntimeException("Unauthorized");
		}

		return taskRepository.findByAssignedUserId(userId);	

	}

	public Task updateTaskStatus(Long taskId, String status, Long currentUserId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

		if (!task.getAssignedUser().getId().equals(currentUserId)
				&& !task.getProject().getUser().getId().equals(currentUserId)) {
			throw new RuntimeException("Unauthorized");
		}
		
		if (status == null || status.isBlank()) {
		    throw new RuntimeException("Invalid status");
		}

		task.setStatus(status);

		return taskRepository.save(task);
	}

}

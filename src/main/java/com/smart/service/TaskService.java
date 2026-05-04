package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.entity.Project;
import com.smart.entity.Task;
import com.smart.entity.User;
import com.smart.exception.BadRequestException;
import com.smart.exception.ResourceNotFoundException;
import com.smart.exception.UnauthorizedException;
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
	
	@Transactional()
	public Task createTask(Task task, Long currentUserId) {
		Project project = projectRepository.findById(task.getProject().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Project Not Found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("UnAthorized");
		}

		User assignedUser = userRepository.findById(task.getAssignedUser().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Assigned User Not Found"));

		if (!assignedUser.getId().equals(project.getUser().getId())) {
		    throw new ResourceNotFoundException("Assigned user must belong to project");
		}
		
		task.setProject(project);
		task.setAssignedUser(assignedUser);

		return taskRepository.save(task);	 
		
	}

	@Transactional(readOnly=true)
	public List<Task> getTasksByProject(Long projectId, Long currentUserId) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}
		return taskRepository.findByProjectId(projectId);

	}

	@Transactional(readOnly=true)
	public List<Task> getTasksByUser(Long userId, Long currentUserId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		if (!userId.equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}

		return taskRepository.findByAssignedUserId(userId);	

	}
   @Transactional
	public Task updateTaskStatus(Long taskId, String status, Long currentUserId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task Not Found"));

		if (!task.getAssignedUser().getId().equals(currentUserId)
				&& !task.getProject().getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}
		
		if (status == null || status.isBlank()) {
		    throw new BadRequestException("Invalid status");
		}

		task.setStatus(status);

		return taskRepository.save(task);
	}

}

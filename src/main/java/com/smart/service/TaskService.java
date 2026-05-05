package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.dto.TaskRequestDTO;
import com.smart.dto.TaskResponseDTO;
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
	
	private TaskResponseDTO convertToDTO(Task task) {
	    TaskResponseDTO dto = new TaskResponseDTO();

	    dto.setId(task.getId());
	    dto.setTitle(task.getTitle());
	    dto.setDescription(task.getDescription());
	    dto.setStatus(task.getStatus());
	    dto.setPriority(task.getPriority());
	    dto.setDueDate(task.getDueDate());

	    dto.setProjectId(task.getProject().getId());
	    dto.setAssignedUserId(task.getAssignedUser().getId());

	    return dto;
	}
	
	@Transactional()
	public TaskResponseDTO createTask(TaskRequestDTO task, Long currentUserId) {
		Project project = projectRepository.findById(task.getProjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Project Not Found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("UnAthorized");
		}

		User assignedUser = userRepository.findById(task.getAssignedUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Assigned User Not Found"));

		if (!assignedUser.getId().equals(project.getUser().getId())) {
		    throw new ResourceNotFoundException("Assigned user must belong to project");
		}
		Task task1=new Task();
		task1.setTitle(task.getTitle());
		task1.setDescription(task.getDescription());
		task1.setStatus(task.getStatus());
		task1.setDueDate(task.getDueDate());
		task1.setPriority(task.getPriority());
		task1.setProject(project);
		task1.setAssignedUser(assignedUser);

		Task task2= taskRepository.save(task1);	 
		
		return convertToDTO(task2);
		
	}

	@Transactional(readOnly=true)
	public List<TaskResponseDTO> getTasksByProject(Long projectId, Long currentUserId) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));

		if (!project.getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}
		 List<Task> tasks=taskRepository.findByProjectId(projectId);
		 
		 return tasks.stream()
				 	.map(task -> convertToDTO(task))
				 	.toList();

	}

	@Transactional(readOnly=true)
	public List<TaskResponseDTO> getTasksByUser(Long userId, Long currentUserId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (!userId.equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}

		List<Task> tasks= taskRepository.findByAssignedUserId(userId);	
		
		return tasks.stream()
				  .map(task -> convertToDTO(task))
				  .toList();

	}
   @Transactional
	public TaskResponseDTO updateTaskStatus(Long taskId, String status, Long currentUserId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task Not Found"));

		if (!task.getAssignedUser().getId().equals(currentUserId)
				&& !task.getProject().getUser().getId().equals(currentUserId)) {
			throw new UnauthorizedException("Unauthorized");
		}
		
		if (status == null || status.isBlank()) {
		    throw new BadRequestException("Invalid status");
		}

		task.setStatus(status);

		Task task1= taskRepository.save(task);
		
		return convertToDTO(task1);
	}

}

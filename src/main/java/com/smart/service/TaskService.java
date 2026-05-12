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
import com.smart.security.AuthenticatedUserService;



@Service
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final AuthenticatedUserService authenticatedUserService;

	public TaskService(TaskRepository taskRepository, UserRepository userRepository,
			ProjectRepository projectRepository,AuthenticatedUserService authenticatedUserService) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.authenticatedUserService=authenticatedUserService;
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
	
	private Project getOwnedProject(Long ProjectId) {
		User currentUser=authenticatedUserService.getCurrentUser();
		
		Project project=projectRepository.findById(ProjectId).orElseThrow(()->new ResourceNotFoundException("Project Not Found"));
		
		if(!project.getUser().getId().equals(currentUser.getId())) {
			throw new UnauthorizedException("Unauthorized");
		}
		return project;
	}
	
	private Task getAuthorizedTask(Long taskId) {
		User currentUser=authenticatedUserService.getCurrentUser();
		
		Task task=taskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException("Task not Found"));
		
		boolean isAssignedUser=task.getAssignedUser().getId().equals(currentUser.getId());
		
		boolean isProjectOwner=task.getProject().getUser().getId().equals(currentUser.getId());
		
		if(!isAssignedUser && !isProjectOwner) {
			throw new UnauthorizedException("Unathorized");
		}
		return task;
	}
	
	
	@Transactional()
	public TaskResponseDTO createTask(TaskRequestDTO task) {
		Project project=getOwnedProject(task.getProjectId());

		User assignedUser = userRepository.findById(task.getAssignedUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Assigned User Not Found"));

		
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
	public List<TaskResponseDTO> getTasksByProject(Long projectId) {

	   Project project=getOwnedProject(projectId);
	   
		 List<Task> tasks=taskRepository.findByProjectId(projectId);
		 
		 return tasks.stream()
				 	.map(task -> convertToDTO(task))
				 	.toList();

	}

	@Transactional(readOnly=true)
	public List<TaskResponseDTO> getTasksByUser() {


		User user=authenticatedUserService.getCurrentUser();
		List<Task> tasks= taskRepository.findByAssignedUserId(user.getId());	
		
		return tasks.stream()
				  .map(task -> convertToDTO(task))
				  .toList();

	}
   @Transactional
	public TaskResponseDTO updateTaskStatus(Long taskId, String status) {
	
       Task task=getAuthorizedTask(taskId);
		if (status == null || status.isBlank()) {
		    throw new BadRequestException("Invalid status");
		}

		task.setStatus(status);

		Task task1= taskRepository.save(task);
		
		return convertToDTO(task1);
	}

}

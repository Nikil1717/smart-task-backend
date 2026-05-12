package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smart.dto.ProjectRequestDTO;
import com.smart.dto.ProjectResponseDTO;
import com.smart.entity.Project;
import com.smart.entity.User;
import com.smart.exception.BadRequestException;
import com.smart.exception.ResourceNotFoundException;
import com.smart.repository.ProjectRepository;
import com.smart.repository.UserRepository;
import com.smart.security.AuthenticatedUserService;

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final AuthenticatedUserService authenticatedUserService;
	
	public ProjectService(ProjectRepository projectRepository,UserRepository userRepository,AuthenticatedUserService authenticatedUserService) {
		this.projectRepository=projectRepository;
		this.authenticatedUserService=authenticatedUserService;
				
	}
	
	public ProjectResponseDTO convertToDTO(Project project) {
		ProjectResponseDTO project1=new ProjectResponseDTO();
		
		project1.setDescription(project.getDescription());
		project1.setId(project.getId());
		project1.setName(project.getName());
		project1.setUserId(project.getUser().getId());
		
		return project1;
	}
	
	private Project getProjectIfOwnedByCurrentUser(Long projectId) {

	    User currentUser =
	            authenticatedUserService.getCurrentUser();

	    Project project =
	            projectRepository.findById(projectId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Project not found"
	                            ));

	    if (!project.getUser().getId().equals(currentUser.getId())) {
	        throw new BadRequestException(
	                "You are not allowed to access this project"
	        );
	    }

	    return project;
	}
	
	
	public ProjectResponseDTO createProject(ProjectRequestDTO project) {

	 User currentUser=authenticatedUserService.getCurrentUser();

	    Project project1=new Project();
	    project1.setName(project.getName());
	    project1.setDescription(project.getDescription());
	    project1.setUser(currentUser);

	    Project project2= projectRepository.save(project1);
	    
	    return convertToDTO(project2);
	}
	
	public List<ProjectResponseDTO> getMyProjects(){
		
		User currentUser =authenticatedUserService.getCurrentUser();
		
		List<Project> projects= projectRepository.findByUserId(currentUser.getId());
		
		return projects.stream()
					.map(project -> convertToDTO(project))
					.toList();
		
	}
	public ProjectResponseDTO getProjectById(Long projectId) {

	    Project project =
	            getProjectIfOwnedByCurrentUser(projectId);

	    return convertToDTO(project);
	}
	
	public ProjectResponseDTO updateProject(Long projectId,ProjectRequestDTO dto) {
		Project project=getProjectIfOwnedByCurrentUser(projectId);
		
		project.setName(dto.getName());
		project.setDescription(dto.getDescription());
		
		Project updatedProject=projectRepository.save(project);
		
		return convertToDTO(updatedProject);
	}
	
	public void deleteProject(Long projectId) {
		Project project=getProjectIfOwnedByCurrentUser(projectId);
		
		projectRepository.deleteById(projectId);
	}
}

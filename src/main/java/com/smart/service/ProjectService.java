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

@Service
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	
	public ProjectService(ProjectRepository projectRepository,UserRepository userRepository) {
		this.projectRepository=projectRepository;
		this.userRepository=userRepository;
				
	}
	
	public ProjectResponseDTO convertToDTO(Project project) {
		ProjectResponseDTO project1=new ProjectResponseDTO();
		
		project1.setDescription(project.getDescription());
		project1.setId(project.getId());
		project1.setName(project.getName());
		project1.setUserId(project.getUser().getId());
		
		return project1;
	}
	
	
	public ProjectResponseDTO createProject(ProjectRequestDTO project) {

	    if (project.getUserId() == null) {
	        throw new BadRequestException("User ID is required");
	    }

	    User user = userRepository.findById(project.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    Project project1=new Project();
	    project1.setName(project.getName());
	    project1.setDescription(project.getDescription());
	    project1.setUser(user);

	    Project project2= projectRepository.save(project1);
	    
	    return convertToDTO(project2);
	}
	
	public List<ProjectResponseDTO> getProjectsByUser(Long userId){
		
		List<Project> projects= projectRepository.findByUserId(userId);
		
		return projects.stream()
					.map(project -> convertToDTO(project))
					.toList();
		
	}
}

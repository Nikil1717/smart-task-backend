package com.smart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smart.entity.Project;
import com.smart.entity.User;
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
	
	public Project createProject(Project project) {

	    if (project.getUser() == null || project.getUser().getId() == null) {
	        throw new RuntimeException("User ID is required");
	    }

	    User user = userRepository.findById(project.getUser().getId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    project.setUser(user);

	    return projectRepository.save(project);
	}
	
	public List<Project> getProjectsByUser(Long userId){
		
		return projectRepository.findByUserId(userId);
		
	}
}

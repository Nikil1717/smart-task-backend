package com.smart.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.smart.dto.ProjectRequestDTO;
import com.smart.dto.ProjectResponseDTO;
import com.smart.dto.UserResponseDTO;
import com.smart.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    // Constructor Injection (same pattern you used before)
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

   
    @PostMapping
    public ProjectResponseDTO createProject( @Valid @RequestBody ProjectRequestDTO request) {
    	
        return projectService.createProject(request);
    }

   
    @GetMapping("/my")
    public List<ProjectResponseDTO> getProjectsByUser() {
        return projectService.getMyProjects();
    }
    
    @GetMapping("/{projectId}")
    public ProjectResponseDTO getProjectById(
            @PathVariable Long projectId
    ) {
        return projectService.getProjectById(projectId);
    }
    
    @PutMapping("/{projectId}")
	public ProjectResponseDTO updateProject(@PathVariable Long projectId,@RequestBody ProjectRequestDTO dto) {
		return projectService.updateProject(projectId, dto);
	}
    
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
    	projectService.deleteProject(projectId);
    }
	
   
}
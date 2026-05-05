package com.smart.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.smart.dto.ProjectRequestDTO;
import com.smart.dto.ProjectResponseDTO;
import com.smart.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    // Constructor Injection (same pattern you used before)
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

   
    @PostMapping
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO request) {
    	System.out.println("Controller hit");
    	System.out.println("Request: " + request);
        return projectService.createProject(request);
    }

   
    @GetMapping("/user/{userId}")
    public List<ProjectResponseDTO> getProjectsByUser(@PathVariable Long userId) {
        return projectService.getProjectsByUser(userId);
    }
    
    @GetMapping("/test")
    public String test() {
        return "working";
    }
}
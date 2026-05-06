package com.smart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.UserRequestDTO;
import com.smart.dto.UserResponseDTO;
import com.smart.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping
	public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request) {
		return userService.createUser(request);
	}
	
	@GetMapping("/id/{userId}")
	public UserResponseDTO getUserById(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}
	
	@GetMapping("/email/{email}")
	public UserResponseDTO getUserByEmail(@PathVariable String email) {
		return userService.getUserByEmail(email);
	}
	
	
}

package com.smart.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.dto.UserRequestDTO;
import com.smart.dto.UserResponseDTO;
import com.smart.entity.User;
import com.smart.exception.ResourceNotFoundException;
import com.smart.repository.UserRepository;

@Service
public class UserService {

	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	private UserResponseDTO convertToDTO(User user) {
		UserResponseDTO userResponse=new UserResponseDTO();
		
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getName());
		userResponse.setId(user.getId());
		userResponse.setRole(user.getRole());
		
		return userResponse;
	}
	
	public UserResponseDTO createUser(UserRequestDTO userRequest) {
		 User user=new User();
		 user.setEmail(userRequest.getEmail());
		 user.setName(userRequest.getName());
		 String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
		 user.setPassword(hashedPassword);
		 user.setRole(userRequest.getRole());
		 User user1= userRepository.save(user);
		 return convertToDTO(user1);
		
	}
	
	public UserResponseDTO getUserById(Long id) {
		User user= userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		return convertToDTO(user);
	
	}
	
	public UserResponseDTO getUserByEmail(String email) {
		
		User user= userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		return convertToDTO(user);
		
	}
}

package com.smart.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.dto.UserRequestDTO;
import com.smart.dto.UserResponseDTO;
import com.smart.dto.UserUpdateRequestDTO;
import com.smart.entity.User;
import com.smart.exception.ResourceNotFoundException;
import com.smart.repository.UserRepository;
import com.smart.security.AuthenticatedUserService;



@Service
public class UserService {

	
	private final UserRepository userRepository;
	private final AuthenticatedUserService authenticatedUserService;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,AuthenticatedUserService authenticatedUserService) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.authenticatedUserService=authenticatedUserService;
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
		 user.setRole("ROLE_USER");
		 User user1= userRepository.save(user);
		 return convertToDTO(user1);
		
	}
	
	@Cacheable(value = "users",
			key="#id")
	public UserResponseDTO getUserById(Long id) {
		
		User user= userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		return convertToDTO(user);
	
	}
	
	public UserResponseDTO getUserByEmail(String email) {
		
		User user= userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		return convertToDTO(user);
		
	}
	
	public UserResponseDTO getMyProfile() {
		User currentUser=authenticatedUserService.getCurrentUser();
		return convertToDTO(currentUser);
	}
	
	@CacheEvict(value ="users",key="#result.id")
	public UserResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
		User currentUser=authenticatedUserService.getCurrentUser();
		
		currentUser.setName(userUpdateRequestDTO.getName());
		currentUser.setEmail(userUpdateRequestDTO.getEmail());
		
		User updatedUser=userRepository.save(currentUser);
		return convertToDTO(updatedUser);
	}
}

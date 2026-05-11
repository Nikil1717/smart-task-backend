package com.smart.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smart.entity.User;
import com.smart.exception.ResourceNotFoundException;
import com.smart.repository.UserRepository;

@Service
public class AuthenticatedUserService {

	private final UserRepository userRepository;
	
	public AuthenticatedUserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	public String getCurrentUserEmail() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		return authentication.getName();
	}
	
	public User getCurrentUser() {
		
		String email=getCurrentUserEmail();
		
		return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
	}
}

package com.smart.service;


import org.springframework.stereotype.Service;

import com.smart.entity.User;
import com.smart.repository.UserRepository;

@Service
public class UserService {

	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	public User createUser(User user) {
		 return userRepository.save(user);
		
	}
	
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(()->new RuntimeException("User Not Found"));
		
	
	}
	
	public User getUserByEmail(String email) {
		
		return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found"));
		
	}
}

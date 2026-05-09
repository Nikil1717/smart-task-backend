package com.smart.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.AuthRequestDTO;
import com.smart.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public AuthController(AuthenticationManager authenticationManager,JwtService jwtService) {
		this.authenticationManager=authenticationManager;
				this.jwtService=jwtService;
	}
	
	@PostMapping("/login")
	public String login(@RequestBody @Valid AuthRequestDTO request) {
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		String token =
		        jwtService.generateToken(request.getEmail());
		return token;
	}
}

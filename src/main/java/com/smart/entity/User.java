package com.smart.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="users")
public class User {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // telling postgres to create id
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank // it is application level check before hitting db
	@Column(unique = true,nullable = false)// this is better it will handle at database level 
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String role;
	@CreationTimestamp
	private Timestamp createdAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "User [Id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", createdAt=" + createdAt + "]";
	}
	
	
	
	
}

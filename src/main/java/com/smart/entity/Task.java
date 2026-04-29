package com.smart.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="tasks")
public class Task {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 @NotBlank
	 @Column(nullable = false)
	 private String title;
	
	 private String description;
	 @NotBlank
	 @Column(nullable = false)
	 private String status;
	 @NotBlank
	 @Column(nullable = false)
	 private String priority;
     @NotNull
     @Column(nullable = false)
	 private LocalDate dueDate;
     @CreationTimestamp
	 private LocalDateTime createdAt;
     @ManyToOne
     @JoinColumn(name="project_id",nullable=false)
	 private Project project;
     @ManyToOne
     @JoinColumn(name="assigned_user_id",nullable=false)
	 private User assignedUser;
     
	 public Long getId() {
		 return id;
	 }
	 public void setId(Long id) {
		 this.id = id;
	 }
	 public String getTitle() {
		 return title;
	 }
	 public void setTitle(String title) {
		 this.title = title;
	 }
	 public String getDescription() {
		 return description;
	 }
	 public void setDescription(String description) {
		 this.description = description;
	 }
	 public String getStatus() {
		 return status;
	 }
	 public void setStatus(String status) {
		 this.status = status;
	 }
	 public String getPriority() {
		 return priority;
	 }
	 public void setPriority(String priority) {
		 this.priority = priority;
	 }
	 public LocalDate getDueDate() {
		 return dueDate;
	 }
	 public void setDueDate(LocalDate dueDate) {
		 this.dueDate = dueDate;
	 }
	 public LocalDateTime getCreatedAt() {
		 return createdAt;
	 }
	 public void setCreatedAt(LocalDateTime createdAt) {
		 this.createdAt = createdAt;
	 }
	 public Project getProject() {
		 return project;
	 }
	 public void setProject(Project project) {
		 this.project = project;
	 }
	 public User getAssignedUser() {
		 return assignedUser;
	 }
	 public void setAssignedUser(User assignedUser) {
		 this.assignedUser = assignedUser;
	 }
	 @Override
	 public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", priority=" + priority + ", dueDate=" + dueDate + ", createdAt=" + createdAt + ", projectId="
				+ (project!=null? project.getId():null) + ", assignedUser=" + (assignedUser!=null?assignedUser.getId():null) + "]";
	 }
     
     
     
}

package com.smart.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequestDTO {

		@NotBlank
	    private String title;
		@NotBlank
	    private String description;
		@NotBlank
	    private String status;
		@NotBlank
	    private String priority;
		@NotNull
	    private LocalDate dueDate;

		@NotNull
	    private Long projectId;
		@NotNull
	    private Long assignedUserId;
	    
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
		public Long getProjectId() {
			return projectId;
		}
		public void setProjectId(Long projectId) {
			this.projectId = projectId;
		}
		public Long getAssignedUserId() {
			return assignedUserId;
		}
		public void setAssignedUserId(Long assignedUserId) {
			this.assignedUserId = assignedUserId;
		}
	    
}

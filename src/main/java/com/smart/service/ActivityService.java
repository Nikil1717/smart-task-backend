package com.smart.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

	@Async
	public void logTaskCreation(
			String taskTitle,String assignedUser) {
		
		try {
			System.out.println("Starting Background activity logging...");
			
			Thread.sleep(5000);
			
			System.out.println("Task Created:" + taskTitle + " assigned to " + assignedUser);
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}

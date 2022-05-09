package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class RegistrationService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(User user) {
		
		return userRepository.save(user);
		
		
	}
      
	public User fetchUserByEmailId(String email) {
		
		return userRepository.findByEmailId(email);
	}
	
public User fetchUserByEmailIdAndPassword(String email, String password) {
		
		return userRepository.findByEmailIdAndPassword(email, password);
	}
}

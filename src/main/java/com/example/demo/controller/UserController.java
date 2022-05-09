 package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RegistrationService;

// origins = "http://localhost:4200"
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	private RegistrationService service;
	
	@GetMapping("/register")
	//@RequestMapping(value="/api/v1/register", method= RequestMethod.GET)
	public User registerUser(@RequestBody User user)  throws Exception {
		String tempEmailId = user.getEmailId();
		if(tempEmailId != null && !"".equals(tempEmailId) ) {
			User userObj = service.fetchUserByEmailId(tempEmailId);
			if(userObj	!= null) {
				
				throw new Exception("user with "+tempEmailId+" is already exist");
			}
		}
		User userObj = null;
		userObj = service.saveUser(user);
		return userObj;
	}
	
	@PostMapping("/login")
	//@RequestMapping(value="/api/v1/login", method= RequestMethod.POST)
	public User loginUser(@RequestBody User user)  throws Exception{
		String tempEmailId = user.getEmailId();
		String tempPass = user.getPassword();
		User userObj = null;
		if(tempEmailId != null && tempPass != null) {
			userObj = service.fetchUserByEmailIdAndPassword(tempEmailId, tempPass);
			
		}
		if(userObj == null) {
			throw new Exception("Bad Credentials");
		}
		
		return userObj;
	}
	
/*	@GetMapping("/login")
	public User loginUser(@PathVariable ("email_id") String emailId, String password)
	{
		return userRepository.findByEmailIdAndPassword(emailId, password);
	}
	*/
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}

	//create employee rest api
	@PostMapping("/users")
	public User CreateUser(@RequestBody User user) {
		return userRepository.save(user);
		
		
	}
	
	//get employee by id rest
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));
		return ResponseEntity.ok(user);
				
		
	}
	
	//update employee res api
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody User userDetails){
		if( userRepository.existsById(id)) {
		User user = userRepository.findById(id)
				.orElseThrow (() -> new EntityNotFoundException("User not exist with id :" + id ));
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setEmailId(userDetails.getEmailId());
		
		User updateUser = userRepository.save(user);
		  return ResponseEntity.ok().body(user);
		
	}
	else {
		HashMap<String, String> message = new HashMap<>();
		message.put("message", id + "user not found or matched");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	}
	//delete 
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user not exist with id :" + id));
		
		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
		
	}
	 
}

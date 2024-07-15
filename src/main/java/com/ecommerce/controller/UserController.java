package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;


@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService; 
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
		User user =userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
}

package com.ecommerce.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	

	private JwtProvider jwtProvider;
	
	public UserServiceImpl(JwtProvider jwtProvider) {
		this.jwtProvider=jwtProvider;
	}
	
	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("User not found with id - "+userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		String email = jwtProvider.getEmailFromToken(jwt);
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new UserException("User not found with email - "+email);
		}
		return user;
	}

}

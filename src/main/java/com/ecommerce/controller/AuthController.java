package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.impl.CustomUserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomUserServiceImpl customUserServiceImpl;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		
		String email = user.getEmail();
		String password = user.getPassword();
		String firstname = user.getFirstName();
		String lastname = user.getLastName();
		
		User isEmailExist = userRepository.findByEmail(email);
		if(isEmailExist!=null) {
			throw new UserException("Email is already used with another account");
		}
		User createUser = new User();
		createUser.setEmail(email);
		createUser.setPassword(passwordEncoder.encode(password));
		createUser.setFirstName(firstname);
		createUser.setLastName(lastname);
		User savedUser = userRepository.save(createUser);
		
		Cart cart = cartService.createCart(savedUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignUp success");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		
	}

	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginReq){
		
		String username = loginReq.getEmail();
		String password = loginReq.getPassword();
		
		Authentication authentication = authenticate(username, password);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignIn success");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
	}


	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
		if(userDetails==null) {
			throw new BadCredentialsException("Username is invalid, Please enter valid username ");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Password is invalid, Please enter valid password");
		}
		
		
		return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
	}
}

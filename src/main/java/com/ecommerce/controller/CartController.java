package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.request.AddItemRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@PutMapping("/add")
	public ResponseEntity<ApiResponse> updateCartHandler(@RequestHeader("Authorization") String jwt,@RequestBody AddItemRequest request) throws UserException, ProductException {
		User user = userService.findUserProfileByJwt(jwt);
		String msg = cartService.addCartItem(user.getId(), request);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage(msg);
		apiResponse.setStatus(true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.CREATED);
	}

	@GetMapping("/")
	public ResponseEntity<Cart> getCart(@RequestHeader("Authorization") String jwt) throws UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Cart cart = cartService.findUserCart(user.getId());
		return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
	}
}

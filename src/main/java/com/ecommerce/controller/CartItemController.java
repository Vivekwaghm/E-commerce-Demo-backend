package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.CartItemService;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/cart_item/")
public class CartItemController {
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/{cartItemId}")
	public ResponseEntity<String> updateCartItemHandler(@PathVariable("cartItemId") Long id,
			@RequestHeader("Authorization") String jwt, @RequestBody CartItem cartItem)
			throws UserException, CartItemException {
		User user = userService.findUserProfileByJwt(jwt);
		CartItem cartItems = cartItemService.updateCartItem(user.getId(), id, cartItem);
		return null;
	}

	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable("cartItemId") Long id,@RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
		
		User user = userService.findUserProfileByJwt(jwt);
		String msg = cartItemService.removeCartItem(user.getId(), id);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage(msg);
		apiResponse.setStatus(true);
		return new ResponseEntity<ApiResponse>(apiResponse , HttpStatus.OK);
	}

}

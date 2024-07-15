package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Order;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrderHandler(){
		List<Order> orders = orderService.getAllOrders();
		return new ResponseEntity<List<Order>>(orders,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable("orderId") Long orderId,
			@RequestHeader("Authorization") String jwt)throws OrderException{
		Order order= orderService.confirmedOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/ship")
	public ResponseEntity<Order> ShippedOrderHandler(@PathVariable("orderId") Long orderId,
			@RequestHeader("Authorization") String jwt)throws OrderException{
		Order order= orderService.shippedOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/deliver")
	public ResponseEntity<Order> DeliveredOrderHandler(@PathVariable("orderId") Long orderId,
			@RequestHeader("Authorization") String jwt)throws OrderException{
		Order order= orderService.deliveredOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> CanceledOrderHandler(@PathVariable("orderId") Long orderId,
			@RequestHeader("Authorization") String jwt)throws OrderException{
		Order order= orderService.cancelledOrder(orderId);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@DeleteMapping("/{orderId}/delete")
	public ResponseEntity<ApiResponse> DeleteOrderHandler(@PathVariable("orderId") Long orderId,
			@RequestHeader("Authorization") String jwt)throws OrderException{
		orderService.deleteOrder(orderId);
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("order deleted successfully");
		apiResponse.setStatus(true);
		return new ResponseEntity<>(apiResponse,HttpStatus.OK);
	}
}

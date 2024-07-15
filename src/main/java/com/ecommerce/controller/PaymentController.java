package com.ecommerce.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Order;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api")
public class PaymentController {

	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@Value("${razorpay.api.secret}")
	private String apiSecret;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException{
		
		Order order = orderService.findOrderById(orderId);
		try {
			RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
			
			JSONObject paymentLinkRequest = new JSONObject();
			paymentLinkRequest.put("amount", order.getTotalPrice()*100);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customer = new JSONObject();
			customer.put("email", order.getUser().getEmail());
			customer.put("name", order.getUser().getFirstName());
			paymentLinkRequest.put("customer", customer);
			
			JSONObject notify = new JSONObject();
			notify.put("sms", true);
			notify.put("email", true);
			
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/"+orderId);
			paymentLinkRequest.put("callback_method", "GET");
			
			PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
			paymentLinkResponse.setPaymentLinkId(paymentLinkId);
			paymentLinkResponse.setPaymentLinkUrl(paymentLinkUrl);
			
			return new ResponseEntity<PaymentLinkResponse>(paymentLinkResponse,HttpStatus.OK);
			
		}catch(Exception e) {
			throw new RazorpayException(e.getMessage());
			
		}	
	}
	
	@GetMapping("/payments")
	public ResponseEntity<ApiResponse> redirect(@RequestParam("payment_id") String paymentId,
			@RequestParam("order_id") Long orderId) throws OrderException, RazorpayException{
		Order order = orderService.findOrderById(orderId); 
		RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
		try {
			Payment payment = razorpay.payments.fetch(paymentId);
			
			if(payment.get("status").equals("captured")) {
				order.getPaymentDetails().setPaymentId(paymentId);
				order.getPaymentDetails().setStatus("Completed");
				order.setOrderStatus("Placed");
				orderRepository.save(order);
			}
			ApiResponse response = new ApiResponse();
			response.setMessage("Your order get placed");
			response.setStatus(true);
			
			return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			throw new RazorpayException(e.getMessage());
		}
		
	}
}

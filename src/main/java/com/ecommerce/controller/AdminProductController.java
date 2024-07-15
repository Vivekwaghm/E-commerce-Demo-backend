package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.request.CreateProductRequest;
import com.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
	
	@Autowired
	public ProductService productService;
	
	
	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProductHandler(@PathVariable("productId") Long productId,@RequestBody Product req) throws ProductException{
		Product product = productService.updateProduct(productId, req);	
		return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/creates")
	public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest req){
		Product product = productService.createProduct(req);
		return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<Product> getAllProductHandler(@RequestBody CreateProductRequest req){
		Product product = productService.createProduct(req);
		return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
	}
	

}

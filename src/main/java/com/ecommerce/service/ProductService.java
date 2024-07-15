package com.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.request.CreateProductRequest;

public interface ProductService {

	public Product createProduct(CreateProductRequest req); 
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product updateProduct(Long productId, Product request) throws ProductException;
	
	public Product findProductById(Long productId) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> size, Integer minPrice, Integer maxPrice,
			Integer minDiscount, String sort,String stock,Integer pageNumber, Integer pageSize);
	
	
}

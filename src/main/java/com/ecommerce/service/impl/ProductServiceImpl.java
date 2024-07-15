package com.ecommerce.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.CreateProductRequest;
import com.ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public Product createProduct(CreateProductRequest req) {

		Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());
		
		if(topLevel==null) {
			Category topLevelCategory = new Category();
			topLevelCategory.setName(req.getTopLavelCategory());
			topLevelCategory.setLevel(1);
			
			topLevel = categoryRepository.save(topLevelCategory);
			System.out.println("Created topLevel-level category: " + topLevel);
		}
		else {
			System.out.println("Found top-level category: " + topLevel);

		}
		
//Category secondLevel = categoryRepository.findByNameAndParent(req.getTopLavelCategory(),topLevel.getName());
//		
//		if(secondLevel==null) {
//			Category secondLevelCategory = new Category();
//			secondLevelCategory.setName(req.getTopLavelCategory());
//			secondLevelCategory.setParentCategory(topLevel);
//			secondLevelCategory.setLevel(2);
//			
//			secondLevel = categoryRepository.save(secondLevelCategory);
//		}
//		
//Category thirdLevel = categoryRepository.findByNameAndParent(req.getTopLavelCategory(),secondLevel.getName());
//		
//		if(thirdLevel==null) {
//			Category thirdLevelCategory = new Category();
//			thirdLevelCategory.setName(req.getTopLavelCategory());
//			thirdLevelCategory.setParentCategory(secondLevel);
//			thirdLevelCategory.setLevel(3);
//			
//			thirdLevel = categoryRepository.save(thirdLevelCategory);
//		}
		 // Second-level category
	    Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLavelCategory(), topLevel);
	    if (secondLevel == null) {
	        Category secondLevelCategory = new Category();
	        secondLevelCategory.setName(req.getSecondLavelCategory());
	        secondLevelCategory.setParentCategory(topLevel);
	        secondLevelCategory.setLevel(2);
	        secondLevel = categoryRepository.save(secondLevelCategory);
	        System.out.println("Created secondLevel-level category: " + secondLevel);
	    }else {
	    	System.out.println("Found second-level category: " + secondLevel);
	    }

	    // Third-level category
	    Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLavelCategory(), secondLevel);
	    if (thirdLevel == null) {
	        Category thirdLevelCategory = new Category();
	        thirdLevelCategory.setName(req.getThirdLavelCategory());
	        thirdLevelCategory.setParentCategory(secondLevel);
	        thirdLevelCategory.setLevel(3);
	        thirdLevel = categoryRepository.save(thirdLevelCategory);
	        System.out.println("Created third-level category: " + thirdLevel);
	    }else {
	    	System.out.println("Found third-level category: " + thirdLevel);
	    }
		Product product = new Product();
		product.setBrand(req.getBrand());
		product.setCategory(thirdLevel);
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountedPercent(req.getDiscountPersent());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setTitle(req.getTitle());
		product.setImageUrl(req.getImageUrl());
		product.setPrice(req.getPrice());
		product.setQuantity(req.getQuantity());
		product.setSizes(req.getSize());
		product.setCreatedAt(LocalDateTime.now());
		
		Product savedProduct = productRepository.save(product);
		
		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		product.getSizes().clear();
		productRepository.delete(product);
		return "Product deleted Successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product request) throws ProductException {
		Product product = findProductById(productId);
		if(request.getQuantity()!=0) {
			product.setQuantity(request.getQuantity());
		}
		return productRepository.save(product);
		
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
		Optional<Product> opt = productRepository.findById(productId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product not found with id - "+productId);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		
		return null;
	}

	@Override
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> size, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		
		if(!colors.isEmpty()) {
			products = products.stream()
					.filter(p -> colors.stream()
							.anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}
		
		if(stock!=null) {
			if(stock.equals("in_stock")) {
				products = products.stream().filter(p -> p.getQuantity()>0)
						.collect(Collectors.toList());
			}
			else if(stock.equals("out_of_stock")){
				products = products.stream().filter(p -> p.getQuantity()<1)
						.collect(Collectors.toList());
			}
		}
		
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(),products.size());
		
		List<Product> pageContent = products.subList(startIndex, endIndex);
		
		Page<Product> filterProducts = new PageImpl<>(pageContent,pageable,products.size());
		
		
		
		return filterProducts;
	}

}

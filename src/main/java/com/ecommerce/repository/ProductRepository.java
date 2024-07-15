package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("SELECT p FROM Product p " +
		       "WHERE (p.category.name = :category OR :category = '') " +
		       "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
		       "AND (:minDiscount IS NULL OR p.discountedPercent >= :minDiscount) " +
		       "ORDER BY " +
		       "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " + // ASC for price_low
		       "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC") // DESC for price_high
		List<Product> filterProducts(
		        @Param("category") String category,
		        @Param("minPrice") Integer minPrice,
		        @Param("maxPrice") Integer maxPrice,
		        @Param("minDiscount") Integer minDiscount,
		        @Param("sort") String sort);
	
	
	
//	@Query("Select p from Product p "
//			+ "where(p.category.name = :category OR :category='') "
//			+ "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discounted_price BETWEEN :minPrice AND :maxPrice)) "
//			+ "AND (:minDiscount IS NULL OR p.discount_percent >= :minDiscount)"
//			+ "ORDER BY "
//			+ "CASE WHEN :sort='price_low' THEN p.discounted_price END ASC "
//			+ "CASE WHEN :sort='price_high' THEN p.discounted_price END DESC")
//	public List<Product> filterProducts(@Param("category") String category,
//			@Param("minPrice") Integer minPrice,
//			@Param("maxPrice") Integer maxPrice,
//			@Param("minDiscount") Integer minDiscount,
//			@Param("sort") String sort);
}

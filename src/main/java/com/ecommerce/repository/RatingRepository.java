package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Rating;

public interface RatingRepository extends JpaRepository<Rating , Long> {

	@Query("select r from Rating as r where r.product.id=:productId")
	public List<Rating> getAllProductsRating(@Param("productId") Long productId);
}

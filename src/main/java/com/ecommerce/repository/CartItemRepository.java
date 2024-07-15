package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	@Query("Select ci from CartItem as ci where ci.cart=:cart and ci.product = :product and ci.size = :size and ci.userId = :userId")
	public CartItem isCardItemExist(@Param("cart") Cart cart, @Param("product") Product product, @Param("size")String size,@Param("userId") Long userId);
}

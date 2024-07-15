package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.OrderItem;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService{

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		return orderItemRepository.save(orderItem);
	}

}

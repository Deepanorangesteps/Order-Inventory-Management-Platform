package com.uphead.order_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	   List<OrderItem> findAllByOrderOrderId(
		        Long orderId
		    );
}

package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.entity.OrderStatus;
import com.uphead.order_management.request.OrderRequest;
import com.uphead.order_management.response.OrderResponse;
import com.uphead.order_management.response.PageResponse;

public interface OrderService {
	
	OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrder(Long orderId);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByStatus(OrderStatus status);

    OrderResponse updateOrderStatus(
        Long orderId,
        OrderStatus status
    );

    void cancelOrder(Long orderId);
    
    PageResponse<OrderResponse> getOrders(
            int page,
            int size,
            OrderStatus status
    );

}

package com.uphead.order_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uphead.order_management.entity.OrderStatus;
import com.uphead.order_management.request.OrderRequest;
import com.uphead.order_management.response.ApiResponse;
import com.uphead.order_management.response.OrderResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder( @Valid @RequestBody OrderRequest request ) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                new ApiResponse<>(
                    true,
                    "Order created successfully",
                    response
                )
            );
    }
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long orderId) {

        OrderResponse response =orderService.getOrder(orderId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Order fetched successfully",
                response
            )
        );
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {

        List<OrderResponse> response = orderService.getAllOrders();

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Orders fetched successfully",
                response
            )
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
        @PathVariable OrderStatus status
    ) {

        List<OrderResponse> response =
            orderService.getOrdersByStatus(status);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Orders fetched successfully",
                response
            )
        );
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status
    ) {

        OrderResponse response =orderService.updateOrderStatus( orderId,status  );

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Order status updated successfully",
                response
            )
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
        @PathVariable Long orderId
    ) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Order cancelled successfully",
                null
            )
        );
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) OrderStatus status
    ) {

        PageResponse<OrderResponse> response =
                orderService.getOrders(
                        page,
                        size,
                        status
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Orders retrieved successfully",
                        response
                )
        );
    }
}

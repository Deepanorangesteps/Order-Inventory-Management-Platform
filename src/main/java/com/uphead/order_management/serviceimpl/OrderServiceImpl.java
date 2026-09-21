package com.uphead.order_management.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uphead.order_management.entity.Customer;
import com.uphead.order_management.entity.Inventory;
import com.uphead.order_management.entity.Order;
import com.uphead.order_management.entity.OrderItem;
import com.uphead.order_management.entity.OrderStatus;
import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.entity.Product;
import com.uphead.order_management.entity.Warehouse;
import com.uphead.order_management.repository.CustomerRepository;
import com.uphead.order_management.repository.InventoryRepository;
import com.uphead.order_management.repository.OrderRepository;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.ProductRepository;
import com.uphead.order_management.repository.WarehouseRepository;
import com.uphead.order_management.request.OrderItemRequest;
import com.uphead.order_management.request.OrderRequest;
import com.uphead.order_management.response.OrderItemResponse;
import com.uphead.order_management.response.OrderResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.OrderService;

@Service
@Transactional

public class OrderServiceImpl implements OrderService{
	 private final OrderRepository orderRepository;
	    private final CustomerRepository customerRepository;
	    private final ProductRepository productRepository;
	    private final WarehouseRepository warehouseRepository;
	    private final InventoryRepository inventoryRepository;
	    private final OrganizationRepository organizationRepository;
	    private final CurrentUserService currentUserService;

	    public OrderServiceImpl(
	        OrderRepository orderRepository,
	        CustomerRepository customerRepository,
	        ProductRepository productRepository,
	        WarehouseRepository warehouseRepository,
	        InventoryRepository inventoryRepository,
	        OrganizationRepository organizationRepository,
	        CurrentUserService currentUserService
	    ) {
	        this.orderRepository = orderRepository;
	        this.customerRepository = customerRepository;
	        this.productRepository = productRepository;
	        this.warehouseRepository = warehouseRepository;
	        this.inventoryRepository = inventoryRepository;
	        this.organizationRepository = organizationRepository;
	        this.currentUserService = currentUserService;
	    }

	@Override
	public OrderResponse createOrder(OrderRequest request) {
		  Long organizationId = currentUserService.getCurrentOrganizationId();
		  Optional<Organization> orgaOptional = Optional.ofNullable(organizationRepository.findByOrganizationId(organizationId).orElseThrow(() ->
	         new RuntimeException(
	                 "Organization not found"
	         )));
		  Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByCustomerIdAndOrganization_OrganizationId(request.getCustomerId(), organizationId))
					.orElseThrow(()->{
						throw new RuntimeException("Customer not found");
					});
		  String orderNumber = generateOrderNumber();
		  while(orderRepository.existsByOrderNumberAndOrganization_OrganizationId(orderNumber, organizationId)) {
			  orderNumber = generateOrderNumber();
		  }
		  Order order = new Order();
    List<OrderItem> orderItemList = new ArrayList<>();
	        order.setOrganization(orgaOptional.get());
	        order.setCustomer(customerOpt.get());
	        order.setOrderNumber(orderNumber);
	        order.setStatus(OrderStatus.PENDING);
	        order.setTotalAmount(BigDecimal.ZERO);
	        BigDecimal totalAmount = BigDecimal.ZERO;

	        for (OrderItemRequest itemRequest : request.getItems()) {
	        	Optional<Product> productOpt = Optional.ofNullable(productRepository.findByProductIdAndOrganization_OrganizationId(itemRequest.getProductId(), organizationId).orElseThrow(() ->
		         new RuntimeException(
		                 "Product not found"
		         )));
	        	Product product = productOpt.get();
	        	if (!Boolean.TRUE.equals(product.getActive())) {
	        		 throw new IllegalArgumentException(
	                         "Product is inactive: "
	                             + product.getProductId()
	                     );
	        	}
	        	
	        	Optional<Warehouse> warehouseOpt = Optional.ofNullable(warehouseRepository.findByWarehouseIdAndOrganization_OrganizationId(itemRequest.getWarehouseId(), organizationId))   .orElseThrow(() ->
	            new RuntimeException(
	                    "Warehouse not found"
	            ));
	        	Warehouse warehouse = warehouseOpt.get();
	        	
	        	 if (!Boolean.TRUE.equals(warehouse.getActive())) {
	                 throw new IllegalArgumentException(
	                     "Warehouse is inactive: "
	                         + warehouse.getWarehouseId()
	                 );
	             }
	        	 
	        	 Optional<Inventory> inventoryOpt = Optional.ofNullable(inventoryRepository
	                     .findByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId(
	                         product.getProductId(),
	                         warehouse.getWarehouseId(),
	                         organizationId
	                     )).orElseThrow(() ->
	     	            new RuntimeException(
	     	            		"Inventory not found for product "
		                                 + product.getProductId()
		                                 + " and warehouse "
		                                 + warehouse.getWarehouseId() ));

	        	 Inventory inventory = inventoryOpt.get();
	        	 int availableQuantity =
	                     inventory.getAvailableQuantity();
	        	 if (availableQuantity < itemRequest.getQuantity()) {
	        		 
	        		 throw new IllegalArgumentException(
	                         "Insufficient inventory for product "
	                             + product.getProductId()
	                             + ". Available quantity: "
	                             + availableQuantity
	                     );
	        		  

	        	 } 
	        	 BigDecimal unitPrice = product.getPrice();
	        	 BigDecimal subtotal =
	                     unitPrice.multiply(
	                         BigDecimal.valueOf(
	                             itemRequest.getQuantity()
	                         )
	                     );

	                 OrderItem orderItem = new OrderItem();

	                 orderItem.setProduct(product);
	                 orderItem.setWarehouse(warehouse);
	                 orderItem.setQuantity(itemRequest.getQuantity());
	                 orderItem.setUnitPrice(unitPrice);
	                 orderItem.setSubtotal(subtotal);

	                 order.addOrderItem(orderItem);

	                 inventory.setReservedQuantity(
	                     inventory.getReservedQuantity()
	                         + itemRequest.getQuantity()
	                 );

	                 inventoryRepository.save(inventory);

	                 totalAmount = totalAmount.add(subtotal);

	        }
	

	        order.setTotalAmount(totalAmount);

	        Order savedOrder = orderRepository.save(order);

	        return mapToResponse(savedOrder);
	
		
	}

	@Override
    @Transactional(readOnly = true)
	public OrderResponse getOrder(Long orderId) {
		 Long organizationId  = currentUserService.getCurrentOrganizationId();
		 Optional<Order> orderOpt = Optional.ofNullable(orderRepository.findByOrderIdAndOrganization_OrganizationId(orderId, organizationId))
				 .orElseThrow(() ->
	                new RuntimeException(
	                    "Order not found"
	                )
	            );
		  return mapToResponse(orderOpt.get());
	}

	@Override
	 @Transactional(readOnly = true)
	public List<OrderResponse> getAllOrders() {
		Long organizationId  = currentUserService.getCurrentOrganizationId();
		 return orderRepository
		            .findAllByOrganization_OrganizationId(organizationId)
		            .stream()
		            .map(this::mapToResponse)
		            .toList();
	}

	@Override
	   @Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
		Long organizationId  = currentUserService.getCurrentOrganizationId();
        return orderRepository
                .findAllByOrganization_OrganizationIdAndStatus(
                    organizationId,
                    status
                )
                .stream()
                .map(this::mapToResponse)
                .toList();

	}

	@Override
	@Transactional
	public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
		Long organizationId  = currentUserService.getCurrentOrganizationId();
		Optional<Order> orderOpt = Optional.ofNullable(orderRepository.findByOrderIdAndOrganization_OrganizationId(orderId, organizationId))
				 .orElseThrow(() ->
	                new RuntimeException(
	                    "Order not found"
	                )
	            );
		Order order = orderOpt.get();

		 OrderStatus currentStatus = order.getStatus();
		 if (currentStatus == newStatus) {
		        throw new IllegalArgumentException(
		            "Order is already in " + newStatus + " status"
		        );
		    }
	        if (currentStatus == OrderStatus.COMPLETED) {
	            throw new IllegalArgumentException(
	                "Completed order cannot be updated"
	            );
	        }
	        if (currentStatus == OrderStatus.CANCELLED) {
	            throw new IllegalArgumentException(
	                "Cancelled order cannot be updated"
	            );
	        }
	     // PENDING -> CONFIRMED
	        if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.CONFIRMED) {
	        	  order.setStatus(OrderStatus.CONFIRMED);
	        }
	     // CONFIRMED -> COMPLETED
	        else if (currentStatus == OrderStatus.CONFIRMED   && newStatus == OrderStatus.COMPLETED) {
	        	  completeOrder(order, organizationId);
	        }
	     // PENDING -> COMPLETED
	        else if (currentStatus == OrderStatus.PENDING  && newStatus == OrderStatus.COMPLETED){

	            //  stock was already reserved during creation,
	            // we can directly complete the order.
	            completeOrder(order, organizationId);
	        }else {
	        	throw new IllegalArgumentException(
	                    "Invalid order status transition from "
	                        + currentStatus
	                        + " to "
	                        + newStatus
	                );
	        }
	        Order updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
	}

	private void completeOrder(Order order, Long organizationId) {
		// TODO Auto-generated method stub
		 for (OrderItem orderItem : order.getOrderItems()) {
			 Optional<Inventory> inventoryOpt = Optional.ofNullable(inventoryRepository
                     .findByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId(
                    		 orderItem.getProduct().getProductId(),
                    		 orderItem.getWarehouse().getWarehouseId(),
                         organizationId
                     )).orElseThrow(() ->
     	            new RuntimeException(
     	            		"Inventory not found for product "
	                                 + orderItem.getProduct().getProductId()
	                                 + " and warehouse "
	                                 +  orderItem.getWarehouse().getWarehouseId() ));
			 Inventory inventory = inventoryOpt.get();
			 int orderedQuantity = orderItem.getQuantity();
			 int currentQuantity = inventory.getQuantity();
			 int currentReservedQuantity = inventory.getReservedQuantity();
			 if (currentReservedQuantity < orderedQuantity) {
				   throw new IllegalArgumentException(
			                "Reserved inventory is insufficient for product "
			                    + orderItem.getProduct().getProductId()
			            );
				   
			 }
			 if (currentQuantity < orderedQuantity) {

		            throw new IllegalArgumentException(
		                "Inventory quantity is insufficient for product "
		                    + orderItem.getProduct().getProductId()
		            );
		        }
			 // get the actual stock value 
			 inventory.setQuantity( currentQuantity - orderedQuantity);
			 // Release reservation
		        inventory.setReservedQuantity(currentReservedQuantity - orderedQuantity);
		        inventoryRepository.save(inventory);
			 
		 }
		 order.setStatus(OrderStatus.COMPLETED);
	}

	@Override
	@Transactional
	public void cancelOrder(Long orderId) {
		Long organizationId  = currentUserService.getCurrentOrganizationId();
		Optional<Order> orderOpt = Optional.ofNullable(orderRepository.findByOrderIdAndOrganization_OrganizationId(orderId, organizationId))
				 .orElseThrow(() ->
	                new RuntimeException(
	                    "Order not found"
	                )
	            );
		Order order = orderOpt.get();
		if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                "Cancelled order cannot be updated"
            );
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException(
                "Completed order cannot be updated"
            );
        }
        for (OrderItem orderItem : order.getOrderItems()) {

       	 Optional<Inventory> inventoryOpt = Optional.ofNullable(inventoryRepository
                    .findByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId(
                    		orderItem.getProduct().getProductId(),
                    		 orderItem.getWarehouse().getWarehouseId(),
                        organizationId
                    )).orElseThrow(() ->
    	            new RuntimeException(
                            "Inventory not found"
 ));
       	 Inventory inventory = inventoryOpt.get();
         int newReservedQuantity =
                 inventory.getReservedQuantity()
                     - orderItem.getQuantity();

            if(newReservedQuantity <0) {
            	  newReservedQuantity = 0;
            }
            inventory.setReservedQuantity(newReservedQuantity);
             inventoryRepository.save(inventory);
        }
        
        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
	}
	  private String generateOrderNumber() {

	        return "ORD-"
	            + UUID.randomUUID()
	                .toString()
	                .substring(0, 8)
	                .toUpperCase();
	    }
	  private OrderResponse mapToResponse(Order order) {

	        OrderResponse response = new OrderResponse();

	        response.setOrderId(order.getOrderId());

	        response.setOrganizationId(
	            order.getOrganization()
	                .getOrganizationId()
	        );

	        response.setCustomerId(
	            order.getCustomer()
	                .getCustomerId()
	        );

	        response.setOrderNumber(
	            order.getOrderNumber()
	        );

	        response.setStatus(
	            order.getStatus()
	        );

	        response.setTotalAmount(
	            order.getTotalAmount()
	        );

	        response.setCreatedAt(
	            order.getCreatedAt()
	        );

	        response.setUpdatedAt(
	            order.getUpdatedAt()
	        );

	        List<OrderItemResponse> itemResponses = new ArrayList<>();

	        for (OrderItem item : order.getOrderItems()) {

	            OrderItemResponse itemResponse =
	                new OrderItemResponse();

	            itemResponse.setOrderItemId(
	                item.getOrderItemId()
	            );

	            itemResponse.setProductId(
	                item.getProduct().getProductId()
	            );

	            itemResponse.setWarehouseId(
	                item.getWarehouse().getWarehouseId()
	            );

	            itemResponse.setQuantity(
	                item.getQuantity()
	            );

	            itemResponse.setUnitPrice(
	                item.getUnitPrice()
	            );

	            itemResponse.setSubtotal(
	                item.getSubtotal()
	            );

	            itemResponses.add(itemResponse);
	        }

	        response.setItems(itemResponses);

	        return response;
	  }

	@Override
	public PageResponse<OrderResponse> getOrders(int page, int size, OrderStatus status) {
		Long organizationId  = currentUserService.getCurrentOrganizationId();
		 if (page < 0) {
		        throw new IllegalArgumentException(
		                "Page number cannot be negative"
		        );
		    }

		    if (size < 1 || size > 100) {
		        throw new IllegalArgumentException(
		                "Page size must be between 1 and 100"
		        );
		    }

		    Pageable pageable = PageRequest.of(
		            page,
		            size,
		            Sort.by(
		                    Sort.Direction.DESC,
		                    "createdAt"
		            )
		    );

		    Page<Order> orderPage;

		    if (status == null) {

		        orderPage =
		                orderRepository.findAllByOrganization_OrganizationId(
		                        organizationId,
		                        pageable
		                );

		    } else {

		        orderPage =
		                orderRepository
		                        .findAllByOrganization_OrganizationIdAndStatus(
		                                organizationId,
		                                status,
		                                pageable
		                        );
		    }

		    List<OrderResponse> content =
		            orderPage.getContent()
		                    .stream()
		                    .map(this::mapToResponse)
		                    .toList();

		    return new PageResponse<>(
		            content,
		            orderPage.getNumber(),
		            orderPage.getSize(),
		            orderPage.getTotalElements(),
		            orderPage.getTotalPages(),
		            orderPage.isFirst(),
		            orderPage.isLast()
		    );
	}
}

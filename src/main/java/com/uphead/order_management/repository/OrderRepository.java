package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.Order;
import com.uphead.order_management.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order,Long> {
	
	Optional<Order> findByOrderIdAndOrganization_OrganizationId( Long orderId, Long organizationId ); 
	
	List<Order> findAllByOrganization_OrganizationId( Long organizationId ); 
	
	boolean existsByOrderNumberAndOrganization_OrganizationId( String orderNumber, Long organizationId ); 
	
	List<Order> findAllByOrganization_OrganizationIdAndStatus( Long organizationId, OrderStatus status );

	 Page<Order> findAllByOrganization_OrganizationId(
	            Long organizationId,
	            Pageable pageable
	    );

	    Page<Order> findAllByOrganization_OrganizationIdAndStatus(
	            Long organizationId,
	            OrderStatus status,
	            Pageable pageable
	    );

	 

	   
}

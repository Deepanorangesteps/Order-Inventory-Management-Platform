package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCustomerIdAndOrganization_OrganizationId( Long customerId, Long organizationId ); 

	List<Customer> findAllByOrganization_OrganizationId( Long organizationId ); 

	boolean existsByEmailAndOrganization_OrganizationId( String email, Long organizationId );
	
	 Page<Customer> findAllByOrganization_OrganizationId(
	            Long organizationId,
	            Pageable pageable
	    );

	 Page<Customer> findByOrganization_OrganizationIdAndNameContainingIgnoreCase(
	            Long organizationId,
	            String name,
	            Pageable pageable
	    );
}

package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Optional<Product> findByProductIdAndOrganization_OrganizationId(
            Long productId,
            Long organizationId
    );

    List<Product> findAllByOrganization_OrganizationId(
            Long organizationId
    );

    boolean existsBySkuAndOrganization_OrganizationId(
            String sku,
            Long organizationId
    );
    
    Page<Product> findAllByOrganization_OrganizationId( Long organizationId, Pageable pageable );
    
    Page<Product> findByOrganization_OrganizationIdAndNameContainingIgnoreCase( Long organizationId, String name, Pageable pageable );
    
    

}

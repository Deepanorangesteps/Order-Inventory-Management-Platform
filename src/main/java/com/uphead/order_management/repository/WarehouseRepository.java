package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>{
	  Optional<Warehouse> findByWarehouseIdAndOrganization_OrganizationId(
	            Long warehouseId,
	            Long organizationId
	    );

	    List<Warehouse> findAllByOrganization_OrganizationId(
	            Long organizationId
	    );

	    boolean existsByCodeAndOrganization_OrganizationId(
	            String code,
	            Long organizationId
	    );
	    
	    Page<Warehouse> findAllByOrganization_OrganizationId(
	            Long organizationId,
	            Pageable pageable
	    );

	    Page<Warehouse> findByOrganization_OrganizationIdAndNameContainingIgnoreCase(
	            Long organizationId,
	            String name,
	            Pageable pageable
	    );

}

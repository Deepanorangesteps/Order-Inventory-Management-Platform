package com.uphead.order_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uphead.order_management.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	
	Optional<Organization> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);
    
    Optional<Organization> findByOrganizationId(Long organizationId);

}

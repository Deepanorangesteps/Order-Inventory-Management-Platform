package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uphead.order_management.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { 
	Optional<User> findByEmail(String email);

Optional<User> findByEmailAndOrganization_OrganizationId(
        String email,
        Long organizationId
);

boolean existsByEmailAndOrganization_OrganizationId(
        String email,
        Long organizationId
);

boolean existsByEmailAndOrganization_Code(
        String email,
        String code
);

Optional<User> findByUserIdAndOrganization_OrganizationId(
        Long userId,
        Long organizationId
);

Optional<User> findByUserId(Long userId,Long organizationId);

List<User> findAllByOrganization_OrganizationId(Long organizationId);
}

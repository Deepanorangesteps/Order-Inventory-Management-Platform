package com.uphead.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import com.uphead.order_management.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByInventoryIdAndOrganization_OrganizationId(
            Long inventoryId,
            Long organizationId
    );

    List<Inventory> findAllByOrganization_OrganizationId(
            Long organizationId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId(
            Long productId,
            Long warehouseId,
            Long organizationId
    );

    boolean existsByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId(
            Long productId,
            Long warehouseId,
            Long organizationId
    );
    
    Page<Inventory> findAllByOrganization_OrganizationId(
            Long organizationId,
            Pageable pageable
    );

    Page<Inventory> findAllByOrganization_OrganizationIdAndProductProductId(
            Long organizationId,
            Long productId,
            Pageable pageable
    );

    Page<Inventory> findAllByOrganization_OrganizationIdAndWarehouseWarehouseId(
            Long organizationId,
            Long warehouseId,
            Pageable pageable
    );

    Page<Inventory> findAllByOrganization_OrganizationIdAndProductProductIdAndWarehouseWarehouseId(
            Long organizationId,
            Long productId,
            Long warehouseId,
            Pageable pageable
    );
    
    
}

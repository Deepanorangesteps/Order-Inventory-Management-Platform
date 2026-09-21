package com.uphead.order_management.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uphead.order_management.entity.Inventory;
import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.entity.Product;
import com.uphead.order_management.entity.Warehouse;
import com.uphead.order_management.repository.InventoryRepository;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.ProductRepository;
import com.uphead.order_management.repository.WarehouseRepository;
import com.uphead.order_management.request.InventoryRequest;
import com.uphead.order_management.response.InventoryResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService{
	private static final Logger log =
			LoggerFactory.getLogger(InventoryServiceImpl.class);

	private final InventoryRepository inventoryRepository;
	private final OrganizationRepository organizationRepository;
	private final ProductRepository productRepository;
	private final WarehouseRepository warehouseRepository;
	private final CurrentUserService currentUserService;

	public InventoryServiceImpl(
			InventoryRepository inventoryRepository,
			OrganizationRepository organizationRepository,
			ProductRepository productRepository,
			WarehouseRepository warehouseRepository,
			CurrentUserService currentUserService) {

		this.inventoryRepository = inventoryRepository;
		this.organizationRepository = organizationRepository;
		this.productRepository = productRepository;
		this.warehouseRepository = warehouseRepository;
		this.currentUserService = currentUserService;
	}

	@Override
	public InventoryResponse createInventory(InventoryRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Product> productOpt = Optional.ofNullable(productRepository.findByProductIdAndOrganization_OrganizationId(request.getProductId(), organizationId)).orElseThrow(() ->
		new RuntimeException(
				"Product not found"
				));
		Optional<Warehouse> warehouseOpt = Optional.ofNullable(warehouseRepository.findByWarehouseIdAndOrganization_OrganizationId(request.getWarehouseId(), organizationId))   .orElseThrow(() ->
		new RuntimeException(
				"Warehouse not found"
				));
		Optional<Organization> orgaOptional = Optional.ofNullable(organizationRepository.findByOrganizationId(organizationId).orElseThrow(() ->
		new RuntimeException(
				"Organization not found"
				)));
		boolean exists =inventoryRepository.existsByProduct_ProductIdAndWarehouse_WarehouseIdAndOrganization_OrganizationId
				(request.getProductId(), request.getWarehouseId(), organizationId);
		if (exists) {
			log.warn("Inventory already exists for Product ID: {} and Warehouse ID: {}", request.getProductId(),
					request.getWarehouseId()
					);

			throw new RuntimeException(
					"Inventory already exists for this product and warehouse"
					);
		}
		// 5. Create inventory
		Inventory inventory = new Inventory();

		inventory.setOrganization(orgaOptional.get());
		inventory.setProduct(productOpt.get());
		inventory.setWarehouse(warehouseOpt.get());
		inventory.setQuantity(request.getQuantity());
		inventory.setReservedQuantity(0);

		Inventory savedInventory =
				inventoryRepository.save(inventory);

		log.info(
				"Inventory created successfully. Inventory ID: {}",
				savedInventory.getInventoryId()
				);
		return mapToResponse(savedInventory);
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponse getInventoryById(Long inventoryId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Inventory> inventory = Optional.ofNullable(inventoryRepository.findByInventoryIdAndOrganization_OrganizationId(inventoryId, organizationId))
				.orElseThrow(() ->
				new RuntimeException(
						"Inventory not found"
						));
		return mapToResponse(inventory.get());
	} 

	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponse> getAllInventory() {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		return inventoryRepository.findAllByOrganization_OrganizationId(organizationId).stream().
				map(this::mapToResponse).toList();
	}

	@Override
	@Transactional
	public InventoryResponse updateInventory(Long inventoryId, InventoryRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Inventory> inventoryOpt = Optional.ofNullable(inventoryRepository.findByInventoryIdAndOrganization_OrganizationId(inventoryId, organizationId))
				.orElseThrow(() ->
				new RuntimeException(
						"Inventory not found"
						));

		/*
		 * Do not allow changing the product/warehouse
		 * of an existing inventory record.
		 * Inventory belongs to a specific
		 * Product + Warehouse combination.
		 */
		Inventory inventory =inventoryOpt.get();
		if(!inventory.getProduct().getProductId().equals(request.getProductId())) {
			throw new IllegalArgumentException(
					"Product cannot be changed for existing inventory"
					);
		}
		if(!inventory.getWarehouse().getWarehouseId().equals(request.getWarehouseId())) {
			throw new IllegalArgumentException(
					"Warehouse cannot be changed for existing inventory"
					);
		}
		/*
		 * Prevent quantity from being less than
		 * already reserved quantity.
		 */

		if(request.getQuantity()<inventory.getReservedQuantity()) {
			throw new IllegalArgumentException(
					"Quantity cannot be less than reserved quantity"
					);
		}
		inventory.setQuantity(request.getQuantity());
		Inventory updatedInventory = inventoryRepository.save(inventory);
		return mapToResponse(updatedInventory);
	}

	@Override
	@Transactional
	public void deleteInventory(Long inventoryId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Inventory> inventoryOpt = Optional.ofNullable(inventoryRepository.findByInventoryIdAndOrganization_OrganizationId(inventoryId, organizationId))
				.orElseThrow(() ->
				new RuntimeException(
						"Inventory not found"
						));

		/*
		 * Do not delete inventory when stock
		 * is currently reserved.
		 */
		Inventory inve = inventoryOpt.get();
		if(inve.getReservedQuantity()>0) {
			throw new IllegalArgumentException(
					"Cannot delete inventory with reserved stock"
					);
		}
		inventoryRepository.delete(inve);


	}
	private InventoryResponse mapToResponse(Inventory inventory) {
		InventoryResponse response = new InventoryResponse();
		response.setInventoryId(inventory.getInventoryId());
		response.setOrganizationId(inventory.getOrganization().getOrganizationId());
		response.setOrganizationCode(inventory.getOrganization().getCode());
		response.setProductId(inventory.getProduct().getProductId());
		response.setProdcutCode(inventory.getProduct().getName());
		response.setWarehouseId(inventory.getWarehouse().getWarehouseId());
		response.setWarehouseCode(inventory.getWarehouse().getCode());
		response.setQuantity(inventory.getQuantity());
		response.setReservedQuantity(inventory.getReservedQuantity());
		response.setAvailableQuantity(inventory.getAvailableQuantity());
		response.setCreatedAt(inventory.getCreatedAt());
		response.setUpdatedAt(inventory.getUpdatedAt());
		return response;


	}

	@Override
	public PageResponse<InventoryResponse> getInventory(int page, int size, Long productId, Long warehouseId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();

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

	    Page<Inventory> inventoryPage;

	    if (productId != null && warehouseId != null) {

	        inventoryPage =
	                inventoryRepository
	                        .findAllByOrganization_OrganizationIdAndProductProductIdAndWarehouseWarehouseId(
	                                organizationId,
	                                productId,
	                                warehouseId,
	                                pageable
	                        );

	    } else if (productId != null) {

	        inventoryPage =
	                inventoryRepository
	                        .findAllByOrganization_OrganizationIdAndProductProductId(
	                                organizationId,
	                                productId,
	                                pageable
	                        );

	    } else if (warehouseId != null) {

	        inventoryPage =
	                inventoryRepository
	                        .findAllByOrganization_OrganizationIdAndWarehouseWarehouseId(
	                                organizationId,
	                                warehouseId,
	                                pageable
	                        );

	    } else {

	        inventoryPage =
	                inventoryRepository.findAllByOrganization_OrganizationId(
	                        organizationId,
	                        pageable
	                );
	    }

	    List<InventoryResponse> content =
	            inventoryPage.getContent()
	                    .stream()
	                    .map(this::mapToResponse)
	                    .toList();

	    return new PageResponse<>(
	            content,
	            inventoryPage.getNumber(),
	            inventoryPage.getSize(),
	            inventoryPage.getTotalElements(),
	            inventoryPage.getTotalPages(),
	            inventoryPage.isFirst(),
	            inventoryPage.isLast()
	    );
	}


}

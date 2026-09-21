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

import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.entity.Warehouse;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.WarehouseRepository;
import com.uphead.order_management.request.WarehouseRequest;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.WarehouseResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.WarehouseService;

@Service
public class WarehouseServiceImpl implements WarehouseService{

    private static final Logger log =
            LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseRepository warehouseRepository;
    private final OrganizationRepository organizationRepository;
    private final CurrentUserService currentUserService;

    public WarehouseServiceImpl(
            WarehouseRepository warehouseRepository,
            OrganizationRepository organizationRepository,
            CurrentUserService currentUserService) {

        this.warehouseRepository = warehouseRepository;
        this.organizationRepository = organizationRepository;
        this.currentUserService = currentUserService;
    }

	@Override
	 @Transactional
	public WarehouseResponse createWarehouse(WarehouseRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		
		 if (warehouseRepository.existsByCodeAndOrganization_OrganizationId(request.getCode(),organizationId)) {
			 throw new RuntimeException("Warehouse with this code already exists");
			 
		 }
		 Optional<Organization> orgaOptional = Optional.ofNullable(organizationRepository.findByOrganizationId(organizationId).orElseThrow(() ->
         new RuntimeException(
                 "Organization not found"
         )));
		  Warehouse warehouse = new Warehouse();

	        warehouse.setOrganization(orgaOptional.get());
	        warehouse.setName(request.getName());
	        warehouse.setCode(request.getCode());
	        warehouse.setAddress(request.getAddress());
	        warehouse.setActive(request.getActive() != null ? request.getActive(): true);
	        Warehouse savedWarehouse =
	                warehouseRepository.save(warehouse);
		return mapToResponse(savedWarehouse);
	}

	@Override
	 @Transactional(readOnly = true)
	public WarehouseResponse getWarehouseById(Long warehouseId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		 Optional<Warehouse> warehouse = Optional.ofNullable(warehouseRepository.findByWarehouseIdAndOrganization_OrganizationId(warehouseId, organizationId))   .orElseThrow(() ->
         new RuntimeException(
                 "Warehouse not found"
         ));
		 return mapToResponse(warehouse.get());
	}

	@Override
	  @Transactional(readOnly = true)
	public List<WarehouseResponse> getAllWarehouses() {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		return warehouseRepository
                .findAllByOrganization_OrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
	}

	@Override
	 @Transactional
	public WarehouseResponse updateWarehouse(Long warehouseId, WarehouseRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Warehouse> warehouseOpt = Optional.ofNullable(warehouseRepository.findByWarehouseIdAndOrganization_OrganizationId(warehouseId, organizationId))   .orElseThrow(() ->
        new RuntimeException(
                "Warehouse not found"
        ));
		if (warehouseRepository.existsByCodeAndOrganization_OrganizationId(request.getCode(),organizationId)) {
			 throw new RuntimeException("Warehouse with this code already exists");
			 
		 }
		Warehouse warehouse = warehouseOpt.get();
		
		 warehouse.setName(request.getName());
	        warehouse.setCode(request.getCode());
	        warehouse.setAddress(request.getAddress());

	        if (request.getActive() != null) {
	            warehouse.setActive(request.getActive());
	        }

	        Warehouse updatedWarehouse =
	                warehouseRepository.save(warehouse);

	        return mapToResponse(updatedWarehouse);
	}

	@Override
	 @Transactional
	public void deleteWarehouse(Long warehouseId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Warehouse> warehouseOpt = Optional.ofNullable(warehouseRepository.findByWarehouseIdAndOrganization_OrganizationId(warehouseId, organizationId))   .orElseThrow(() ->
        new RuntimeException(
                "Warehouse not found"
        ));
		warehouseRepository.delete(warehouseOpt.get());
		
	}
	
	 private WarehouseResponse mapToResponse( Warehouse warehouse) {
		 WarehouseResponse response =new WarehouseResponse();
		 response.setCode(warehouse.getCode());
		 response.setOrganizationId(warehouse.getOrganization().getOrganizationId());
		 response.setName(warehouse.getName());
		 response.setAddress(warehouse.getAddress());
		 response.setActive(warehouse.getActive());
         response.setCreatedAt(warehouse.getCreatedAt());
         return response;
	 }

	@Override
	public PageResponse<WarehouseResponse> getWarehouses(int page, int size, String search) {
		Long organizationId = currentUserService.getCurrentOrganizationId();

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

		    Page<Warehouse> warehousePage;

		    if (search == null || search.trim().isEmpty()) {

		        warehousePage =
		                warehouseRepository.findAllByOrganization_OrganizationId(
		                        organizationId,
		                        pageable
		                );

		    } else {

		        warehousePage =
		                warehouseRepository
		                        .findByOrganization_OrganizationIdAndNameContainingIgnoreCase(
		                                organizationId,
		                                search.trim(),
		                                pageable
		                        );
		    }

		    List<WarehouseResponse> content =
		            warehousePage.getContent()
		                    .stream()
		                    .map(this::mapToResponse)
		                    .toList();

		    return new PageResponse<>(
		            content,
		            warehousePage.getNumber(),
		            warehousePage.getSize(),
		            warehousePage.getTotalElements(),
		            warehousePage.getTotalPages(),
		            warehousePage.isFirst(),
		            warehousePage.isLast()
		    );
	}

}

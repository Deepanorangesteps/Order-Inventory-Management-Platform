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
import com.uphead.order_management.entity.Product;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.repository.ProductRepository;
import com.uphead.order_management.request.ProductRequest;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.ProductResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	  private static final Logger log =
	            LoggerFactory.getLogger(ProductServiceImpl.class);

	    private final ProductRepository productRepository;
	    private final OrganizationRepository organizationRepository;
	    private final CurrentUserService currentUserService;

	    public ProductServiceImpl(
	            ProductRepository productRepository,
	            OrganizationRepository organizationRepository,
	            CurrentUserService currentUserService) {

	        this.productRepository = productRepository;
	        this.organizationRepository = organizationRepository;
	        this.currentUserService = currentUserService;
	    }

	@Override
	@Transactional
	public ProductResponse createProduct(ProductRequest request) {
	     Long organizationId = currentUserService.getCurrentOrganizationId();
	     log.info(  "Creating product with SKU: {} for organization: {}",
	                request.getSku(),
	                organizationId);

	     if(productRepository.existsBySkuAndOrganization_OrganizationId(request.getSku(), organizationId)) {
	    	 throw new RuntimeException(" Product with this SKU already exists");
	     }
	     Optional<Organization> orgaOptional = Optional.ofNullable(organizationRepository.findByOrganizationId(organizationId).orElseThrow(() ->
         new RuntimeException(
                 "Organization not found"
         )));
	     
	     Product product = new Product();
	     product.setOrganization(orgaOptional.get());
	        product.setName(request.getName());
	        product.setSku(request.getSku());
	        product.setDescription(request.getDescription());
	        product.setPrice(request.getPrice());
	        product.setActive(request.getActive() !=null? request.getActive():true);
	        Product savedProduct =productRepository.save(product);
	        return mapToResponse(savedProduct);
	}

	 @Override
	@Transactional(readOnly = true)
	public ProductResponse getProductById(Long id) {
		 Long organizationId  = currentUserService.getCurrentOrganizationId();
		 Optional<Product> product = Optional.ofNullable(productRepository.findByProductIdAndOrganization_OrganizationId(id, organizationId).orElseThrow(() ->
         new RuntimeException(
                 "Product not found"
         )));
		
		 return mapToResponse(product.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {
		   Long organizationId =currentUserService.getCurrentOrganizationId();
		   List<Product> productList = productRepository.findAllByOrganization_OrganizationId(organizationId);
		   return productList.stream()
	                .map(this::mapToResponse)
	                .toList();
	}

	@Override
	@Transactional
	public ProductResponse updateProduct(Long productId, ProductRequest request) {
		 Long organizationId =currentUserService.getCurrentOrganizationId();
		 Optional<Product> productOpt = Optional.ofNullable(productRepository.findByProductIdAndOrganization_OrganizationId(productId, organizationId)).orElseThrow(() ->
         new RuntimeException(
                 "Product not found"
         ));
		 Product product = productOpt.get();
		 if(product.getSku().equals(request.getSku())&&
				 productRepository.existsBySkuAndOrganization_OrganizationId(request.getSku(), organizationId)) {
			 throw new RuntimeException("Product with this SKU already exists");
		 }
		 product.setName(request.getName());
	        product.setSku(request.getSku());
	        product.setDescription(request.getDescription());
	        product.setPrice(request.getPrice());
	        if (request.getActive() != null) {
	            product.setActive(request.getActive());
	        }

	        Product updatedProduct =
	                productRepository.save(product);
	        log.info("Product updated successfully Product ID: {}",productId);
	        return mapToResponse(updatedProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		 Long organizationId =currentUserService.getCurrentOrganizationId();
		 Optional<Product> productOpt = Optional.ofNullable(productRepository.findByProductIdAndOrganization_OrganizationId(id, organizationId)).orElseThrow(() ->
         new RuntimeException(
                 "Product not found"
         ));
		 productRepository.delete(productOpt.get());
		
	}
	
	private ProductResponse mapToResponse(Product product) {
		
		ProductResponse response = new ProductResponse();

        response.setId(product.getProductId());
        response.setOrganizationId(
                product.getOrganization().getOrganizationId()
        );
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
	}

	@Override
	public PageResponse<ProductResponse> getProducts(int page, int size, String search) {
		 Long organizationId =currentUserService.getCurrentOrganizationId();
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

		    Page<Product> productPage;

		    if (search == null || search.trim().isEmpty()) {

		        productPage =
		                productRepository.findAllByOrganization_OrganizationId(
		                        organizationId,
		                        pageable
		                );

		    } else {

		        productPage =
		                productRepository
		                        .findByOrganization_OrganizationIdAndNameContainingIgnoreCase(
		                                organizationId,
		                                search.trim(),
		                                pageable
		                        );
		    }

		    List<ProductResponse> content =
		            productPage.getContent()
		                    .stream()
		                    .map(this::mapToResponse)
		                    .toList();

		    return new PageResponse<>(
		            content,
		            productPage.getNumber(),
		            productPage.getSize(),
		            productPage.getTotalElements(),
		            productPage.getTotalPages(),
		            productPage.isFirst(),
		            productPage.isLast()
		    );
	}

}

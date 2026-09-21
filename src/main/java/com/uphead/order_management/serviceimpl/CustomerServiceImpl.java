package com.uphead.order_management.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uphead.order_management.entity.Customer;
import com.uphead.order_management.entity.Organization;
import com.uphead.order_management.repository.CustomerRepository;
import com.uphead.order_management.repository.OrganizationRepository;
import com.uphead.order_management.request.CustomerRequest;
import com.uphead.order_management.response.CustomerResponse;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.security.CurrentUserService;
import com.uphead.order_management.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
	
	  private final CustomerRepository customerRepository;
	    private final OrganizationRepository organizationRepository;
	    private final CurrentUserService currentUserService;

	    public CustomerServiceImpl(
	        CustomerRepository customerRepository,
	        OrganizationRepository organizationRepository,
	        CurrentUserService currentUserService
	    ) {
	        this.customerRepository = customerRepository;
	        this.organizationRepository = organizationRepository;
	        this.currentUserService = currentUserService;
	    }


	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		if(customerRepository.existsByEmailAndOrganization_OrganizationId(request.getEmail(), organizationId)) {
			throw new RuntimeException( "Customer with email already exists");
		}
		 Optional<Organization> orgaOptional = Optional.ofNullable(organizationRepository.findByOrganizationId(organizationId).orElseThrow(() ->
         new RuntimeException(
                 "Organization not found"
         )));
		 
		 Customer customer = new Customer();

	        customer.setOrganization(orgaOptional.get());
	        customer.setName(request.getName());
	        customer.setEmail(request.getEmail());
	        customer.setPhone(request.getPhone());
	        customer.setAddress(request.getAddress());
	        if (request.getActive() != null) {
	            customer.setActive(request.getActive());
	        }

	        Customer savedCustomer = customerRepository.save(customer);

	        return mapToResponse(savedCustomer);
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerResponse getCustomer(Long customerId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByCustomerIdAndOrganization_OrganizationId(customerId, organizationId))
				.orElseThrow(()->{
					throw new RuntimeException("Customer not found");
				});
		
		 return mapToResponse(customerOpt.get());
	}

	@Override
	public List<CustomerResponse> getAllCustomers() {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		return customerRepository.findAllByOrganization_OrganizationId(organizationId)
				.stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	public CustomerResponse updateCustomer(Long customerId, CustomerRequest request) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByCustomerIdAndOrganization_OrganizationId(customerId, organizationId))
				.orElseThrow(()->{
					throw new RuntimeException("Customer not found");
				});
		Customer customer = customerOpt.get();
		 if (!customer.getEmail().equals(request.getEmail())
				 && customerRepository.existsByEmailAndOrganization_OrganizationId( request.getEmail(), organizationId
			                )) {
			 throw new RuntimeException(
		                "Customer with email already exists"
		            );
			 
		 }
		
		 customer.setName(request.getName());
	        customer.setEmail(request.getEmail());
	        customer.setPhone(request.getPhone());
	        customer.setAddress(request.getAddress());

	        if (request.getActive() != null) {
	            customer.setActive(request.getActive());
	        }

	        Customer updatedCustomer =
	            customerRepository.save(customer);

	        return mapToResponse(updatedCustomer);
	}

	@Override
	public void deleteCustomer(Long customerId) {
		Long organizationId = currentUserService.getCurrentOrganizationId();
		Optional<Customer> customerOpt = Optional.ofNullable(customerRepository.findByCustomerIdAndOrganization_OrganizationId(customerId, organizationId))
				.orElseThrow(()->{
					throw new RuntimeException("Customer not found");
				});
	    customerRepository.delete(customerOpt.get());
		
	}
	  private CustomerResponse mapToResponse(Customer customer) {
	        CustomerResponse response = new CustomerResponse();
	        response.setCustomerId(customer.getCustomerId());
	        response.setOrganizationId(
	            customer.getOrganization().getOrganizationId()
	        );
	        response.setName(customer.getName());
	        response.setEmail(customer.getEmail());
	        response.setPhone(customer.getPhone());
	        response.setAddress(customer.getAddress());
	        response.setActive(customer.getActive());
	        response.setCreatedAt(customer.getCreatedAt());
	        response.setUpdatedAt(customer.getUpdatedAt());

	        return response;
	    }


	@Override
	public PageResponse<CustomerResponse> getCustomers(int page, int size, String search) {
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

		    Page<Customer> customerPage;

		    if (search == null || search.trim().isEmpty()) {

		        customerPage =
		                customerRepository.findAllByOrganization_OrganizationId(
		                        organizationId,
		                        pageable
		                );

		    } else {

		        customerPage =
		                customerRepository
		                        .findByOrganization_OrganizationIdAndNameContainingIgnoreCase(
		                                organizationId,
		                                search.trim(),
		                                pageable
		                        );
		    }

		    List<CustomerResponse> content =
		            customerPage.getContent()
		                    .stream()
		                    .map(this::mapToResponse)
		                    .toList();

		    return new PageResponse<>(
		            content,
		            customerPage.getNumber(),
		            customerPage.getSize(),
		            customerPage.getTotalElements(),
		            customerPage.getTotalPages(),
		            customerPage.isFirst(),
		            customerPage.isLast()
		    );
	}
}

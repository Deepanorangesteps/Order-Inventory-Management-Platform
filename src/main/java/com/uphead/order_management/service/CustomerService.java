package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.request.CustomerRequest;
import com.uphead.order_management.response.CustomerResponse;
import com.uphead.order_management.response.PageResponse;

public interface CustomerService {
	
	 CustomerResponse createCustomer(CustomerRequest request);

	    CustomerResponse getCustomer(Long customerId);

	    List<CustomerResponse> getAllCustomers();

	    CustomerResponse updateCustomer(
	        Long customerId,
	        CustomerRequest request
	    );

	    void deleteCustomer(Long customerId);
	    
	    PageResponse<CustomerResponse> getCustomers(
	            int page,
	            int size,
	            String search
	    );

}

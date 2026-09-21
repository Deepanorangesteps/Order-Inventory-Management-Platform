package com.uphead.order_management.service;

import java.util.List;

import com.uphead.order_management.request.ProductRequest;
import com.uphead.order_management.response.PageResponse;
import com.uphead.order_management.response.ProductResponse;

public interface ProductService {
	

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
    
    PageResponse<ProductResponse> getProducts(
            int page,
            int size,
            String search
    );

}

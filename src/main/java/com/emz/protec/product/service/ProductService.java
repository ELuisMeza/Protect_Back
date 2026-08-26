package com.emz.protec.product.service;

import java.util.List;

import com.emz.protec.product.dto.ProductRequest;
import com.emz.protec.product.dto.ProductResponse;

public interface ProductService {

	List<ProductResponse> findAll(Long categoryId);

	ProductResponse findById(Long id);

	ProductResponse create(ProductRequest request);

	ProductResponse update(Long id, ProductRequest request);

	void delete(Long id);
}

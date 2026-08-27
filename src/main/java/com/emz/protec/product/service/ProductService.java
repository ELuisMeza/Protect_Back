package com.emz.protec.product.service;

import com.emz.protec.product.dto.ProductPageResponse;
import com.emz.protec.product.dto.ProductRequest;
import com.emz.protec.product.dto.ProductResponse;

public interface ProductService {

	ProductPageResponse findAll(Long categoryId, String search, Long page, Long limit);

	ProductResponse findById(Long id);

	ProductResponse create(ProductRequest request);

	ProductResponse update(Long id, ProductRequest request);

	void delete(Long id);
}

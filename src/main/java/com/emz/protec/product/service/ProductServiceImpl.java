package com.emz.protec.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.domain.Category;
import com.emz.protec.category.repository.CategoryRepository;
import com.emz.protec.exception.ResourceNotFoundException;
import com.emz.protec.product.domain.Product;
import com.emz.protec.product.dto.ProductRequest;
import com.emz.protec.product.dto.ProductResponse;
import com.emz.protec.product.mapper.ProductMapper;
import com.emz.protec.product.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductMapper productMapper;

	public ProductServiceImpl(
			ProductRepository productRepository,
			CategoryRepository categoryRepository,
			ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productMapper = productMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> findAll(Long categoryId) {
		List<Product> products;
		if (categoryId == null) {
			products = productRepository.findByActiveTrueWithCategory();
		} else {
			products = productRepository.findByActiveTrueAndCategoryId(categoryId);
		}
		return products.stream().map(productMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		Product product = productRepository.findByIdAndActiveTrue(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
		return productMapper.toResponse(product);
	}

	@Override
	public ProductResponse create(ProductRequest request) {
		Category category = findCategory(request.categoryId());
		Product product = productMapper.toEntity(request);
		product.setCategory(category);
		Product saved = productRepository.save(product);
		return productMapper.toResponse(saved);
	}

	@Override
	public ProductResponse update(Long id, ProductRequest request) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
		Category category = findCategory(request.categoryId());
		productMapper.updateEntity(product, request);
		product.setCategory(category);
		return productMapper.toResponse(product);
	}

	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id));
		product.setActive(false);
	}

	private Category findCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + categoryId));
	}
}

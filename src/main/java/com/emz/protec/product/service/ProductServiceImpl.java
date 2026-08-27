package com.emz.protec.product.service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.domain.Category;
import com.emz.protec.category.repository.CategoryRepository;
import com.emz.protec.exception.ResourceNotFoundException;
import com.emz.protec.product.domain.Product;
import com.emz.protec.product.dto.ProductPageResponse;
import com.emz.protec.product.dto.ProductRequest;
import com.emz.protec.product.dto.ProductResponse;
import com.emz.protec.product.mapper.ProductMapper;
import com.emz.protec.product.repository.ProductRepository;
import com.emz.protec.product.repository.ProductSpecifications;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_LIMIT = 20;

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
	public ProductPageResponse findAll(Long categoryId, String search, Long page, Long limit) {
		int pageNumber = page != null ? page.intValue() : DEFAULT_PAGE;
		int pageSize = limit != null ? limit.intValue() : DEFAULT_LIMIT;
		Specification<Product> spec = ProductSpecifications.active();
		if (categoryId != null) {
			spec = spec.and(ProductSpecifications.byCategory(categoryId));
		}
		if (search != null && !search.isBlank()) {
			String searchPattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
			spec = spec.and(ProductSpecifications.nameLike(searchPattern));
		}

		Page<Product> products = productRepository.findAll(
				spec,
				PageRequest.of(
						pageNumber - 1,
						pageSize,
						Sort.by(Sort.Direction.ASC, Product::getName)));

		return new ProductPageResponse(
				products.map(productMapper::toResponse).getContent(),
				products.getTotalElements(),
				pageNumber,
				pageSize,
				products.getTotalPages());
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

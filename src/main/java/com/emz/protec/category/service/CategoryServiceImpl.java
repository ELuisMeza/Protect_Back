package com.emz.protec.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.dto.CategoryRequest;
import com.emz.protec.category.dto.CategoryResponse;
import com.emz.protec.category.repository.CategoryRepository;
import com.emz.protec.category.domain.Category;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryResponse> findAll() {
		return categoryRepository.findAllByOrderByNameAsc().stream()
				.map(category -> new CategoryResponse(category.getId(), category.getName()))
				.toList();
	}

	@Override
	public CategoryResponse create(CategoryRequest request) {
		Category category = categoryRepository.save(new Category(null, request.name()));
		return new CategoryResponse(category.getId(), category.getName());
	}
}

package com.emz.protec.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.dto.CategoryResponse;
import com.emz.protec.category.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<CategoryResponse> findAll() {
		return categoryRepository.findAllByOrderByNameAsc().stream()
				.map(category -> new CategoryResponse(category.getId(), category.getName()))
				.toList();
	}
}

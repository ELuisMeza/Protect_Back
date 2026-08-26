package com.emz.protec.category.service;

import java.util.List;

import com.emz.protec.category.dto.CategoryResponse;

public interface CategoryService {

	List<CategoryResponse> findAll();
}

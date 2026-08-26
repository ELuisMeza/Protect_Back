package com.emz.protec.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emz.protec.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAllByOrderByNameAsc();

	Optional<Category> findByNameIgnoreCase(String name);
}

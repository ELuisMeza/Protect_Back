package com.emz.protec.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emz.protec.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("""
			SELECT p FROM Product p
			JOIN FETCH p.category
			WHERE p.active = true
			ORDER BY p.name ASC
			""")
	List<Product> findByActiveTrueWithCategory();

	@Query("""
			SELECT p FROM Product p
			JOIN FETCH p.category
			WHERE p.active = true AND p.category.id = :categoryId
			ORDER BY p.name ASC
			""")
	List<Product> findByActiveTrueAndCategoryId(@Param("categoryId") Long categoryId);

	@Query("""
			SELECT p FROM Product p
			JOIN FETCH p.category
			WHERE p.id = :id AND p.active = true
			""")
	Optional<Product> findByIdAndActiveTrue(@Param("id") Long id);
}

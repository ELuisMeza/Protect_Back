package com.emz.protec.product.repository;

import org.springframework.data.jpa.domain.Specification;

import com.emz.protec.product.domain.Product;

import jakarta.persistence.criteria.JoinType;

public final class ProductSpecifications {

	private ProductSpecifications() {
	}

	public static Specification<Product> active() {
		return (root, query, cb) -> {
			if (query != null && !Long.class.equals(query.getResultType())) {
				if (root.getFetches().isEmpty()) {
					root.fetch("category", JoinType.INNER);
				}
			}
			return cb.isTrue(root.get("active"));
		};
	}

	public static Specification<Product> byCategory(Long categoryId) {
		return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
	}

	public static Specification<Product> nameLike(String searchPattern) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("name")), searchPattern);
	}
}

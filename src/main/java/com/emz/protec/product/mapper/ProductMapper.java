package com.emz.protec.product.mapper;

import org.springframework.stereotype.Component;

import com.emz.protec.product.domain.Product;
import com.emz.protec.product.dto.ProductRequest;
import com.emz.protec.product.dto.ProductResponse;

@Component
public class ProductMapper {

	public ProductResponse toResponse(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getCategory().getId(),
				product.getCategory().getName(),
				product.getPrice(),
				product.getImageUrl(),
				product.getSpecs(),
				product.isActive());
	}

	public Product toEntity(ProductRequest request) {
		return Product.builder()
				.name(request.name().trim())
				.price(request.price())
				.imageUrl(blankToNull(request.imageUrl()))
				.specs(blankToNull(request.specs()))
				.active(request.active() == null || request.active())
				.build();
	}

	public void updateEntity(Product product, ProductRequest request) {
		product.setName(request.name().trim());
		product.setPrice(request.price());
		product.setImageUrl(blankToNull(request.imageUrl()));
		product.setSpecs(blankToNull(request.specs()));
		if (request.active() != null) {
			product.setActive(request.active());
		}
	}

	private String blankToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}

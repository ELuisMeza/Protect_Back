package com.emz.protec.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
		Long id,
		String name,
		Long categoryId,
		String categoryName,
		BigDecimal price,
		String specs,
		boolean active
) {
}

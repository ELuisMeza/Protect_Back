package com.emz.protec.product.dto;

import java.util.List;

public record ProductPageResponse(
		List<ProductResponse> content,
		long totalElements,
		int page,
		int limit,
		int totalPages
) {
}

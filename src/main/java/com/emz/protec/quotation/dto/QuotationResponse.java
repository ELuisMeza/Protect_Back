package com.emz.protec.quotation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record QuotationResponse(
		Long id,
		String customerName,
		String customerPhone,
		Instant createdAt,
		List<QuotationItemResponse> items,
		BigDecimal total
) {
}

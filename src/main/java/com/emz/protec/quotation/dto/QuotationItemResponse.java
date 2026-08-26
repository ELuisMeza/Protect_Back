package com.emz.protec.quotation.dto;

import java.math.BigDecimal;

public record QuotationItemResponse(
		Long id,
		Long productId,
		String productName,
		Integer quantity,
		BigDecimal unitPrice,
		BigDecimal subtotal
) {
}

package com.emz.protec.quotation.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.emz.protec.quotation.domain.Quotation;
import com.emz.protec.quotation.domain.QuotationItem;
import com.emz.protec.quotation.dto.QuotationItemResponse;
import com.emz.protec.quotation.dto.QuotationResponse;

@Component
public class QuotationMapper {

	public QuotationResponse toResponse(Quotation quotation) {
		List<QuotationItemResponse> items = quotation.getItems().stream()
				.map(this::toItemResponse)
				.toList();

		BigDecimal total = items.stream()
				.map(QuotationItemResponse::subtotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new QuotationResponse(
				quotation.getId(),
				quotation.getCustomerName(),
				quotation.getCustomerPhone(),
				quotation.getCreatedAt(),
				items,
				total);
	}

	public QuotationItemResponse toItemResponse(QuotationItem item) {
		BigDecimal subtotal = item.getUnitPrice()
				.multiply(BigDecimal.valueOf(item.getQuantity()));

		return new QuotationItemResponse(
				item.getId(),
				item.getProduct().getId(),
				item.getProduct().getName(),
				item.getQuantity(),
				item.getUnitPrice(),
				subtotal);
	}
}

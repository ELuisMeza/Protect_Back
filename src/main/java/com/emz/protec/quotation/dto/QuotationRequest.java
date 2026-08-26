package com.emz.protec.quotation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record QuotationRequest(
		@NotBlank(message = "El nombre del cliente es obligatorio")
		String customerName,

		@NotBlank(message = "El teléfono del cliente es obligatorio")
		String customerPhone,

		@NotEmpty(message = "Debe incluir al menos un producto")
		List<@Valid QuotationItemRequest> items
) {
}

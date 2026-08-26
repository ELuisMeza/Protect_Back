package com.emz.protec.quotation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QuotationItemRequest(
		@NotNull(message = "El productId es obligatorio")
		Long productId,

		@NotNull(message = "La cantidad es obligatoria")
		@Min(value = 1, message = "La cantidad debe ser al menos 1")
		Integer quantity
) {
}

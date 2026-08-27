package com.emz.protec.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
		@NotBlank(message = "El nombre es obligatorio")
		String name,

		@NotNull(message = "La categoría es obligatoria")
		Long categoryId,

		@NotNull(message = "El precio es obligatorio")
		@DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
		BigDecimal price,

		String specs,

		Boolean active
) {
}

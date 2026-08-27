package com.emz.protec.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
		@NotBlank(message = "El nombre es obligatorio")
		String name
) {
}

package com.emz.protec.auth.dto;

public record LoginResponse(
		String accessToken,
		String tokenType,
		long expiresIn
) {
}

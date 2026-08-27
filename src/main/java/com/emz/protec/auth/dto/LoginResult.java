package com.emz.protec.auth.dto;

public record LoginResult(
		String token,
		LoginResponse user
) {
}

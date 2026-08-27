package com.emz.protec.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
		String secret,
		long expirationMs,
		String cookieName,
		boolean cookieSecure,
		String cookieSameSite
) {
}

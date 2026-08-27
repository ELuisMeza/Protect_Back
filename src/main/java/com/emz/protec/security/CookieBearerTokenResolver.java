package com.emz.protec.security;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

	private final JwtProperties jwtProperties;

	public CookieBearerTokenResolver(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (jwtProperties.cookieName().equals(cookie.getName())) {
				String value = cookie.getValue();
				return value == null || value.isBlank() ? null : value;
			}
		}
		return null;
	}
}

package com.emz.protec.security;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthCookieService {

	private final JwtProperties jwtProperties;

	public AuthCookieService(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public void setAuthCookie(HttpServletResponse response, String token) {
		response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(token, jwtProperties.expirationMs() / 1000).toString());
	}

	public void clearAuthCookie(HttpServletResponse response) {
		response.addHeader(HttpHeaders.SET_COOKIE, buildCookie("", 0).toString());
	}

	private ResponseCookie buildCookie(String value, long maxAgeSeconds) {
		return ResponseCookie.from(jwtProperties.cookieName(), value)
				.httpOnly(true)
				.secure(jwtProperties.cookieSecure())
				.path("/")
				.maxAge(Duration.ofSeconds(maxAgeSeconds))
				.sameSite(jwtProperties.cookieSameSite())
				.build();
	}
}

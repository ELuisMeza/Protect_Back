package com.emz.protec.whatsapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.green-api")
public record GreenApiProperties(
		String idInstance,
		String apiToken,
		String apiUrl,
		String mediaUrl,
		String defaultCountryCode
) {

	public boolean isConfigured() {
		return notBlank(idInstance) && notBlank(apiToken);
	}

	private static boolean notBlank(String value) {
		return value != null && !value.isBlank();
	}
}

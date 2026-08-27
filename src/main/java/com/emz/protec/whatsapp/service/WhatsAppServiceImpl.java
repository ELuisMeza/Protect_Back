package com.emz.protec.whatsapp.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.emz.protec.exception.BusinessException;
import com.emz.protec.whatsapp.GreenApiProperties;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

	private static final Logger log = LoggerFactory.getLogger(WhatsAppServiceImpl.class);

	private final GreenApiProperties properties;
	private final RestClient restClient;

	public WhatsAppServiceImpl(GreenApiProperties properties) {
		this.properties = properties;
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
		requestFactory.setReadTimeout(Duration.ofSeconds(60));
		this.restClient = RestClient.builder()
				.requestFactory(requestFactory)
				.build();
	}

	@Override
	public void sendText(String phone, String message) {
		if (!ensureConfigured()) {
			return;
		}

		String url = trimSlash(properties.apiUrl())
				+ "/waInstance" + properties.idInstance()
				+ "/sendMessage/" + properties.apiToken();

		postJson(url, Map.of(
				"chatId", toChatId(phone),
				"message", message));
	}

	@Override
	public void sendDocument(String phone, byte[] content, String fileName, String caption) {
		if (!ensureConfigured()) {
			return;
		}
		if (content == null || content.length == 0) {
			throw new BusinessException("El archivo a enviar por WhatsApp está vacío");
		}

		String url = trimSlash(properties.mediaUrl())
				+ "/waInstance" + properties.idInstance()
				+ "/sendFileByUpload/" + properties.apiToken();

		ByteArrayResource fileResource = new ByteArrayResource(content) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("chatId", toChatId(phone));
		body.add("file", fileResource);
		body.add("fileName", fileName);
		if (caption != null && !caption.isBlank()) {
			body.add("caption", caption);
		}

		try {
			restClient.post()
					.uri(url)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.body(body)
					.retrieve()
					.toBodilessEntity();
			log.info("Documento '{}' enviado por WhatsApp a {}", fileName, phone);
		} catch (RestClientResponseException ex) {
			log.error("Green API rechazó el envío del documento: {} {}", ex.getStatusCode(), ex.getResponseBodyAsString());
			throw new BusinessException("No se pudo enviar la cotización por WhatsApp");
		} catch (RestClientException ex) {
			log.error("Error de comunicación con Green API al enviar documento", ex);
			throw new BusinessException("No se pudo enviar la cotización por WhatsApp");
		}
	}

	private void postJson(String url, Map<String, String> payload) {
		try {
			restClient.post()
					.uri(url)
					.contentType(MediaType.APPLICATION_JSON)
					.body(payload)
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientResponseException ex) {
			log.error("Green API rechazó el mensaje de texto: {} {}", ex.getStatusCode(), ex.getResponseBodyAsString());
			throw new BusinessException("No se pudo enviar el mensaje por WhatsApp");
		} catch (RestClientException ex) {
			log.error("Error de comunicación con Green API al enviar texto", ex);
			throw new BusinessException("No se pudo enviar el mensaje por WhatsApp");
		}
	}

	private boolean ensureConfigured() {
		if (properties.isConfigured()) {
			return true;
		}
		log.warn("Green API no está configurada; se omite el envío por WhatsApp");
		return false;
	}

	private static String trimSlash(String url) {
		if (url == null) {
			return "";
		}
		return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
	}

	private String toChatId(String phone) {
		if (phone == null || phone.isBlank()) {
			throw new BusinessException("El teléfono del cliente es obligatorio para WhatsApp");
		}

		String digits = phone.replaceAll("\\D", "");
		if (digits.startsWith("00")) {
			digits = digits.substring(2);
		}

		String countryCode = properties.defaultCountryCode();
		if (countryCode != null && !countryCode.isBlank()
				&& digits.length() <= 9
				&& !digits.startsWith(countryCode)) {
			digits = countryCode + digits;
		}

		if (digits.length() < 10) {
			throw new BusinessException("El teléfono del cliente no es válido para WhatsApp");
		}

		return digits + "@c.us";
	}
}

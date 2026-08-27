package com.emz.protec.whatsapp.service;

public interface WhatsAppService {

	void sendText(String phone, String message);

	void sendDocument(String phone, byte[] content, String fileName, String caption);
}

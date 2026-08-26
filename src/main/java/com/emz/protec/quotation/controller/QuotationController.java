package com.emz.protec.quotation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emz.protec.quotation.dto.QuotationRequest;
import com.emz.protec.quotation.dto.QuotationResponse;
import com.emz.protec.quotation.service.QuotationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quotations")
@Validated
public class QuotationController {

	private final QuotationService quotationService;

	public QuotationController(QuotationService quotationService) {
		this.quotationService = quotationService;
	}

	@PostMapping
	public ResponseEntity<QuotationResponse> create(@Valid @RequestBody QuotationRequest request) {
		QuotationResponse created = quotationService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public ResponseEntity<List<QuotationResponse>> findAll() {
		return ResponseEntity.ok(quotationService.findAll());
	}
}

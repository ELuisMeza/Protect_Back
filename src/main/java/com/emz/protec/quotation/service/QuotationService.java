package com.emz.protec.quotation.service;

import java.util.List;

import com.emz.protec.quotation.dto.QuotationRequest;
import com.emz.protec.quotation.dto.QuotationResponse;

public interface QuotationService {

	QuotationResponse create(QuotationRequest request);

	List<QuotationResponse> findAll();
}

package com.emz.protec.quotation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.exception.ResourceNotFoundException;
import com.emz.protec.product.domain.Product;
import com.emz.protec.product.repository.ProductRepository;
import com.emz.protec.quotation.domain.Quotation;
import com.emz.protec.quotation.domain.QuotationItem;
import com.emz.protec.quotation.dto.QuotationItemRequest;
import com.emz.protec.quotation.dto.QuotationRequest;
import com.emz.protec.quotation.dto.QuotationResponse;
import com.emz.protec.quotation.mapper.QuotationMapper;
// import com.emz.protec.quotation.pdf.QuotationPdfService;
import com.emz.protec.quotation.repository.QuotationRepository;
// import com.emz.protec.whatsapp.service.WhatsAppService;

@Service
@Transactional
public class QuotationServiceImpl implements QuotationService {

	private final QuotationRepository quotationRepository;
	private final ProductRepository productRepository;
	private final QuotationMapper quotationMapper;
	// private final QuotationPdfService quotationPdfService;
	// private final WhatsAppService whatsAppService;

	public QuotationServiceImpl(
			QuotationRepository quotationRepository,
			ProductRepository productRepository,
			QuotationMapper quotationMapper
			// QuotationPdfService quotationPdfService,
			// WhatsAppService whatsAppService
			) {
		this.quotationRepository = quotationRepository;
		this.productRepository = productRepository;
		this.quotationMapper = quotationMapper;
		// this.quotationPdfService = quotationPdfService;
		// this.whatsAppService = whatsAppService;
	}

	@Override
	public QuotationResponse create(QuotationRequest request) {
		Quotation quotation = Quotation.builder()
				.customerName(request.customerName().trim())
				.customerPhone(request.customerPhone().trim())
				.build();

		for (QuotationItemRequest itemRequest : request.items()) {
			Product product = productRepository.findByIdAndActiveTrue(itemRequest.productId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Producto no encontrado o inactivo: " + itemRequest.productId()));

			QuotationItem item = QuotationItem.builder()
					.product(product)
					.quantity(itemRequest.quantity())
					.unitPrice(product.getPrice())
					.build();

			quotation.addItem(item);
		}

		Quotation saved = quotationRepository.save(quotation);
		QuotationResponse response = quotationMapper.toResponse(saved);
		// Envío por WhatsApp desactivado temporalmente
		// sendQuotationPdf(response);
		return response;
	}

	// private void sendQuotationPdf(QuotationResponse quotation) {
	// 	byte[] pdf = quotationPdfService.generate(quotation);
	// 	String fileName = "cotizacion-" + quotation.id() + ".pdf";
	// 	String caption = "Hola " + quotation.customerName()
	// 			+ ", te enviamos tu cotización N.° " + quotation.id()
	// 			+ " de Protec.";
	// 	whatsAppService.sendDocument(quotation.customerPhone(), pdf, fileName, caption);
	// }

	@Override
	@Transactional(readOnly = true)
	public List<QuotationResponse> findAll() {
		return quotationRepository.findAllWithItems().stream()
				.map(quotationMapper::toResponse)
				.toList();
	}
}

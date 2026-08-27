package com.emz.protec.quotation.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.emz.protec.exception.BusinessException;
import com.emz.protec.quotation.dto.QuotationItemResponse;
import com.emz.protec.quotation.dto.QuotationResponse;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class QuotationPdfService {

	private static final Color NAVY = new Color(18, 58, 95);
	private static final Color HEADER_BG = new Color(18, 58, 95);
	private static final Color ROW_ALT = new Color(241, 245, 249);
	private static final Color MUTED = new Color(100, 116, 139);
	private static final ZoneId LIMA = ZoneId.of("America/Lima");
	private static final DateTimeFormatter DATE_FORMAT =
			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(LIMA);

	public byte[] generate(QuotationResponse quotation) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Document document = new Document(PageSize.A4, 40, 40, 40, 40);
			PdfWriter.getInstance(document, out);
			document.open();
			writeContent(document, quotation);
			document.close();
			return out.toByteArray();
		} catch (DocumentException | java.io.IOException ex) {
			throw new BusinessException("No se pudo generar el PDF de la cotización");
		}
	}

	private void writeContent(Document document, QuotationResponse quotation) throws DocumentException {
		Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.WHITE);
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, NAVY);
		Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, MUTED);
		Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
		Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, NAVY);
		Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8, MUTED);

		PdfPTable header = new PdfPTable(1);
		header.setWidthPercentage(100);
		PdfPCell brandCell = new PdfPCell(new Phrase("PROTEC", brandFont));
		brandCell.setBackgroundColor(HEADER_BG);
		brandCell.setBorder(Rectangle.NO_BORDER);
		brandCell.setPadding(16);
		brandCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.addCell(brandCell);
		document.add(header);

		document.add(spacer(12));
		document.add(new Paragraph("Cotización N.° " + quotation.id(), titleFont));
		document.add(spacer(8));

		PdfPTable meta = new PdfPTable(new float[] { 1, 2, 1, 2 });
		meta.setWidthPercentage(100);
		addMeta(meta, "Cliente", quotation.customerName(), labelFont, valueFont);
		addMeta(meta, "Teléfono", quotation.customerPhone(), labelFont, valueFont);
		addMeta(meta, "Fecha", formatDate(quotation), labelFont, valueFont);
		addMeta(meta, "Ítems", String.valueOf(quotation.items().size()), labelFont, valueFont);
		document.add(meta);

		document.add(spacer(16));

		PdfPTable items = new PdfPTable(new float[] { 4.5f, 1.2f, 1.8f, 1.8f });
		items.setWidthPercentage(100);
		addHeaderCell(items, "Producto", headerFont);
		addHeaderCell(items, "Cant.", headerFont);
		addHeaderCell(items, "P. unitario", headerFont);
		addHeaderCell(items, "Subtotal", headerFont);

		boolean alternate = false;
		for (QuotationItemResponse item : quotation.items()) {
			Color bg = alternate ? ROW_ALT : Color.WHITE;
			addBodyCell(items, item.productName(), cellFont, bg, Element.ALIGN_LEFT);
			addBodyCell(items, String.valueOf(item.quantity()), cellFont, bg, Element.ALIGN_CENTER);
			addBodyCell(items, formatMoney(item.unitPrice()), cellFont, bg, Element.ALIGN_RIGHT);
			addBodyCell(items, formatMoney(item.subtotal()), cellFont, bg, Element.ALIGN_RIGHT);
			alternate = !alternate;
		}
		document.add(items);

		document.add(spacer(12));
		Paragraph total = new Paragraph("Total: " + formatMoney(quotation.total()), totalFont);
		total.setAlignment(Element.ALIGN_RIGHT);
		document.add(total);

		document.add(spacer(24));
		Paragraph footer = new Paragraph(
				"Documento generado automáticamente. Precios expresados en soles (PEN).",
				footerFont);
		footer.setAlignment(Element.ALIGN_CENTER);
		document.add(footer);
	}

	private void addMeta(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBorder(Rectangle.NO_BORDER);
		labelCell.setPadding(3);
		table.addCell(labelCell);

		PdfPCell valueCell = new PdfPCell(new Phrase(value == null ? "-" : value, valueFont));
		valueCell.setBorder(Rectangle.NO_BORDER);
		valueCell.setPadding(3);
		table.addCell(valueCell);
	}

	private void addHeaderCell(PdfPTable table, String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBackgroundColor(HEADER_BG);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(8);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
	}

	private void addBodyCell(PdfPTable table, String text, Font font, Color background, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBackgroundColor(background);
		cell.setHorizontalAlignment(alignment);
		cell.setPadding(7);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderWidthBottom(0.4f);
		cell.setBorderColorBottom(new Color(226, 232, 240));
		table.addCell(cell);
	}

	private Paragraph spacer(float leading) {
		Paragraph paragraph = new Paragraph(" ");
		paragraph.setLeading(leading);
		return paragraph;
	}

	private String formatDate(QuotationResponse quotation) {
		if (quotation.createdAt() == null) {
			return "-";
		}
		return DATE_FORMAT.format(quotation.createdAt());
	}

	private String formatMoney(BigDecimal amount) {
		return NumberFormat.getCurrencyInstance(Locale.of("es", "PE"))
				.format(amount == null ? BigDecimal.ZERO : amount);
	}
}

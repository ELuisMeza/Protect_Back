package com.emz.protec.quotation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String customerName;

	@Column(nullable = false, length = 50)
	private String customerPhone;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<QuotationItem> items = new ArrayList<>();

	public void addItem(QuotationItem item) {
		items.add(item);
		item.setQuotation(this);
	}
}

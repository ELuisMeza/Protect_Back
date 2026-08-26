package com.emz.protec.quotation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emz.protec.quotation.domain.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

	@Query("""
			SELECT DISTINCT q FROM Quotation q
			LEFT JOIN FETCH q.items i
			LEFT JOIN FETCH i.product
			ORDER BY q.createdAt DESC
			""")
	List<Quotation> findAllWithItems();
}

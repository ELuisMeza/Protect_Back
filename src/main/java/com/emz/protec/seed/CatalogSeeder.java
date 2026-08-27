package com.emz.protec.seed;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.domain.Category;
import com.emz.protec.category.repository.CategoryRepository;
import com.emz.protec.product.domain.Product;
import com.emz.protec.product.repository.ProductRepository;
import com.emz.protec.seed.CatalogSeedData.SeedProduct;

@Service
public class CatalogSeeder {

	private static final Logger log = LoggerFactory.getLogger(CatalogSeeder.class);

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public CatalogSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public void seed() {
		Map<String, Category> categories = seedCategories();
		seedProducts(categories);
	}

	private Map<String, Category> seedCategories() {
		Map<String, Category> byName = new LinkedHashMap<>();
		int created = 0;
		int existing = 0;

		for (String name : CatalogSeedData.categories()) {
			var found = categoryRepository.findByNameIgnoreCase(name);
			if (found.isPresent()) {
				byName.put(name, found.get());
				existing++;
			} else {
				byName.put(name, categoryRepository.save(Category.builder().name(name).build()));
				created++;
			}
		}

		log.info("Categorías seed: {} creadas, {} ya existían", created, existing);
		return byName;
	}

	private void seedProducts(Map<String, Category> categories) {
		int created = 0;
		int skipped = 0;

		for (SeedProduct item : CatalogSeedData.products()) {
			if (productRepository.existsByNameIgnoreCase(item.name())) {
				skipped++;
				continue;
			}

			Category category = categories.get(item.category());
			if (category == null) {
				throw new IllegalStateException("Categoría no encontrada para seed: " + item.category());
			}

			productRepository.save(Product.builder()
					.name(item.name())
					.category(category)
					.price(new BigDecimal(item.price()))
					.specs(item.specs())
					.active(true)
					.build());
			created++;
		}

		log.info("Productos seed: {} creados, {} omitidos (ya existían)", created, skipped);
	}
}

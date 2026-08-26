package com.emz.protec.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.emz.protec.category.domain.Category;
import com.emz.protec.category.repository.CategoryRepository;
import com.emz.protec.product.domain.Product;
import com.emz.protec.product.repository.ProductRepository;

@Component
public class DataInitializer implements ApplicationRunner {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		seedProducts();
	}

	private void seedProducts() {
		if (productRepository.count() > 0) {
			return;
		}

		Map<String, Category> categories = seedCategories();

		List<Product> products = List.of(
				product(
						"Cámara Domo 4K Full Color",
						categories.get("Cámaras IP"),
						"899.90",
						"https://placehold.co/400x300?text=Camara+Domo+4K",
						"Resolución 4K, visión nocturna a color, PoE, IP67"),
				product(
						"Cámara Bullet 5MP IR",
						categories.get("Cámaras IP"),
						"549.50",
						"https://placehold.co/400x300?text=Camara+Bullet+5MP",
						"5MP, IR 40m, lente fija 3.6mm, carcasa metálica"),
				product(
						"Cámara PTZ 2MP 20x",
						categories.get("Cámaras IP"),
						"1899.00",
						"https://placehold.co/400x300?text=Camara+PTZ",
						"Zoom óptico 20x, tracking automático, PoE+"),
				product(
						"NVR 8 Canales 4K",
						categories.get("DVR/NVR"),
						"1299.00",
						"https://placehold.co/400x300?text=NVR+8CH",
						"8 canales IP, salida HDMI 4K, soporte HDD 2 bahías"),
				product(
						"DVR 16 Canales H.265",
						categories.get("DVR/NVR"),
						"999.00",
						"https://placehold.co/400x300?text=DVR+16CH",
						"16 canales analógicos/IP, compresión H.265+, 2 SATA"),
				product(
						"Disco Duro Vigilancia 4TB",
						categories.get("Accesorios"),
						"429.00",
						"https://placehold.co/400x300?text=HDD+4TB",
						"HDD 4TB 5400rpm optimizado para CCTV 24/7"),
				product(
						"Fuente PoE 8 Puertos",
						categories.get("Accesorios"),
						"319.90",
						"https://placehold.co/400x300?text=PoE+Switch",
						"Switch PoE 8 puertos Fast Ethernet, 120W"),
				product(
						"Kit Cable UTP Cat6 305m",
						categories.get("Accesorios"),
						"259.00",
						"https://placehold.co/400x300?text=UTP+Cat6",
						"Bobina Cat6 CCA 305 metros, exterior"));

		productRepository.saveAll(products);
	}

	private Map<String, Category> seedCategories() {
		List<String> names = List.of("Cámaras IP", "DVR/NVR", "Accesorios");
		return names.stream()
				.map(this::findOrCreateCategory)
				.collect(Collectors.toMap(Category::getName, Function.identity()));
	}

	private Category findOrCreateCategory(String name) {
		return categoryRepository.findByNameIgnoreCase(name)
				.orElseGet(() -> categoryRepository.save(Category.builder().name(name).build()));
	}

	private Product product(
			String name,
			Category category,
			String price,
			String imageUrl,
			String specs) {
		return Product.builder()
				.name(name)
				.category(category)
				.price(new BigDecimal(price))
				.imageUrl(imageUrl)
				.specs(specs)
				.active(true)
				.build();
	}
}

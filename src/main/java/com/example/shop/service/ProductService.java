package com.example.shop.service;

import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> filterAndSortProducts(String category, String brand, Double minPrice, Double maxPrice,
                                               String name, String sortOrder) {
        List<Product> products = productRepository.findByFilters(
                category != null && !category.isEmpty() ? category : "%",
                brand != null && !brand.isEmpty() ? brand : "%",
                minPrice != null ? minPrice : 0.0,
                maxPrice != null ? maxPrice : Double.MAX_VALUE,
                name != null && !name.isEmpty() ? "%" + name.toLowerCase() + "%" : "%"
        );

        // Применяем сортировку, только если sortOrder = asc/desc
        if ("asc".equalsIgnoreCase(sortOrder)) {
            products.sort(Comparator.comparingDouble(Product::getPrice));
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
        }

        return products;
    }

    public double calculateAveragePrice() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);
    }

    public Product findProductWithMaxPrice() {
        return productRepository.findAll().stream()
                .max(Comparator.comparingDouble(Product::getPrice))
                .orElse(null);
    }

    public Product findProductWithMinPrice() {
        return productRepository.findAll().stream()
                .min(Comparator.comparingDouble(Product::getPrice))
                .orElse(null);
    }

    public Map<String, Long> countProductsByCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }
}


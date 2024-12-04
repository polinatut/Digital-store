package com.example.shop.initializer;

import com.example.shop.model.Product;
import com.example.shop.model.User;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Инициализация пользователей
        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            userService.save(admin);
        }

        if (userService.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRole("USER");
            userService.save(user);
        }

        // Инициализация продуктов
        if (productService.findAll().isEmpty()) {
            List<Product> initialProducts = Arrays.asList(
                    new Product("iPhone 14", "Смартфоны различных брендов", "Apple", 25, 90000, "Модель с передовыми функциями", "iPhone 14.jpg"),
                    new Product("Samsung Galaxy S23", "Смартфоны различных брендов", "Samsung", 30, 80000, "Смартфон с высокой производительностью", "samsung s23.jpg"),
                    new Product("MacBook Pro 16", "Ноутбуки", "Apple", 10, 250000, "Высокопроизводительный ноутбук для профессионалов", "MacBook Pro 16.jpg"),
                    new Product("Microsoft Surface Laptop 5", "Ноутбуки", "Microsoft", 12, 150000, "Премиальный ноутбук на Windows", "Microsoft Surface Laptop 5.jpg"),
                    new Product("Apple Watch Series 8", "Умные часы и фитнес-трекеры", "Apple", 40, 40000, "Функции мониторинга здоровья", "Apple Watch Series 8.jpg"),
                    new Product("Samsung Galaxy Watch 5", "Умные часы и фитнес-трекеры", "Samsung", 35, 35000, "Фитнес-трекер с широкими возможностями", "Samsung Galaxy Watch 5.jpg"),
                    new Product("Sony WH-1000XM5", "Наушники (проводные и беспроводные), колонки", "Sony", 50, 26000, "Лучшее шумоподавление на рынке", "Sony WH-1000XM5.jpg"),
                    new Product("Bose 700", "Наушники (проводные и беспроводные), колонки", "Bose", 40, 28000, "Элегантный дизайн и качественный звук", "Bose 700.jpg")
                    );

            initialProducts.forEach(productService::save);
        }
    }
}

package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // Список категорий
    private static final List<String> CATEGORIES = Arrays.asList(
            "Смартфоны различных брендов",
            "Чехлы, защитные стекла, зарядные устройства",
            "Умные часы и фитнес-трекеры",
            "Ноутбуки",
            "Компьютеры и моноблоки",
            "Мыши, клавиатуры, коврики",
            "Мониторы различных размеров и разрешений",
            "Наушники (проводные и беспроводные), колонки",
            "Портативные колонки и музыкальные центры",
            "Саундбары и домашние кинотеатры",
            "Игровые приставки (PlayStation, Xbox, Nintendo)",
            "Игровые аксессуары (геймпады, джойстики, VR-устройства)",
            "Аксессуары для консолей (зарядные станции, контроллеры)",
            "Фотоаппараты, видеокамеры",
            "Умные колонки, камеры видеонаблюдения, лампочки"
    );

    // Отображение списка продуктов для администратора
    @GetMapping
    public String listProductsForAdmin(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin-products";
    }

    // Форма добавления продукта
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", CATEGORIES); // Добавляем список категорий
        return "admin-product-form";
    }

    // Форма редактирования продукта
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products/admin";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", CATEGORIES);
        return "admin-product-form";
    }


    // Сохранение продукта
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute("product") @Valid Product product,
            @RequestParam("image") MultipartFile image,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", CATEGORIES);
            return "admin-product-form";
        }

        try {
            if (!image.isEmpty()) {
                String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get("uploads", imageName);
                Files.createDirectories(imagePath.getParent());
                Files.copy(image.getInputStream(), imagePath);
                product.setImageName(imageName);
            }

            // Обновление или создание нового продукта
            if (product.getId() != null) {
                // Проверяем, существует ли продукт с таким ID
                Product existingProduct = productService.findById(product.getId());
                if (existingProduct != null) {
                    // Обновляем существующий продукт
                    existingProduct.setName(product.getName());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setBrand(product.getBrand());
                    existingProduct.setImageName(product.getImageName());
                    productService.save(existingProduct);
                } else {
                    // Если продукта с таким ID нет, создаём новый
                    productService.save(product);
                }
            } else {
                // Если ID отсутствует, создаём новый продукт
                productService.save(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to upload image.");
            return "admin-product-form";
        }

        return "redirect:/products/admin";
    }


    // Удаление продукта
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products/admin";
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateProductReport() {
        List<Product> products = productService.findAll();

        StringBuilder report = new StringBuilder();
        report.append("Product Report\n");
        report.append("==============\n");

        for (Product product : products) {
            report.append("ID: ").append(product.getId()).append("\n")
                    .append("Name: ").append(product.getName()).append("\n")
                    .append("Category: ").append(product.getCategory()).append("\n")
                    .append("Stock: ").append(product.getStock()).append("\n")
                    .append("-------------------------\n");
        }

        byte[] reportBytes = report.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product_report.txt");

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}
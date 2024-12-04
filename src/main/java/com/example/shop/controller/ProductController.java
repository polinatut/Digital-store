package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

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

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "default") String sortOrder,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            Model model) {

        // Фильтрация и сортировка
        List<Product> products = productService.filterAndSortProducts(category, brand, minPrice, maxPrice, name, sortOrder);

        model.addAttribute("products", products);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("searchName", name);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        // Проверка роли администратора
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "products";
    }


    @GetMapping("/products/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "product-details";
    }


}

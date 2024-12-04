package com.example.shop.controller;

import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stats")
public class StatisticController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String statisticsPage(Model model) {
        model.addAttribute("averagePrice", productService.calculateAveragePrice());
        model.addAttribute("maxPriceProduct", productService.findProductWithMaxPrice());
        model.addAttribute("minPriceProduct", productService.findProductWithMinPrice());
        model.addAttribute("categoryCounts", productService.countProductsByCategory());
        return "statistics";
    }
}


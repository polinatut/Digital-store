package com.example.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController {

    @GetMapping
    public String aboutPage(Model model) {
        model.addAttribute("authorName", "Тютева Полина Петровна");
        model.addAttribute("authorDescription", "Создание информационно-справочной системы магазина цифровой электроники.");
        model.addAttribute("group", "ПИ22-4");
        return "about";
    }
}

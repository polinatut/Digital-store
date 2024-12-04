package com.example.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название товара обязательно")
    @Size(max = 255, message = "Название товара не может превышать 255 символов")
    private String name;

    @NotBlank(message = "Категория обязательна")
    private String category;

    @Size(max = 500, message = "Описание не может превышать 500 символов")
    private String description;

    @Min(value = 0, message = "Количество на складе не может быть отрицательным")
    private int stock;

    @DecimalMin(value = "0.01", inclusive = true, message = "Цена должна быть больше нуля")
    private double price;

    @NotBlank(message = "Бренд обязателен")
    private String brand;

    private String imageName;

    // Конструкторы
    public Product() {
        // Пустой конструктор для JPA
    }

    public Product(String name, String category, String brand, int stock, double price, String description, String imageName) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

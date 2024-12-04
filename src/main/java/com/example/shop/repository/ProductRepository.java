package com.example.shop.repository;

import com.example.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE " +
            "(:category = '%' OR p.category = :category) AND " +
            "(:brand = '%' OR p.brand = :brand) AND " +
            "(p.price >= :minPrice) AND " +
            "(p.price <= :maxPrice) AND " +
            "(LOWER(p.name) LIKE LOWER(:name))")
    List<Product> findByFilters(
            @Param("category") String category,
            @Param("brand") String brand,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("name") String name
    );
}


package com.example.tg_bot_wb.repository;

import com.example.tg_bot_wb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

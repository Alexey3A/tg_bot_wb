package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct();
    Product findByArticle(String article);
    Product saveProduct(Product product, Person person, Message message);
    Product saveProduct(Product product);
    void delete(Person person, String article);
}

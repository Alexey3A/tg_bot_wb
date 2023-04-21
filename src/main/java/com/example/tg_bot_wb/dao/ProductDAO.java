package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;

import java.util.List;

public interface ProductDAO {
    void saveOrUpdateProduct(Product product, Person person);
    void deleteProduct(long id);
    Product getProduct(long id);
    List<Product> getAllProduct();
}

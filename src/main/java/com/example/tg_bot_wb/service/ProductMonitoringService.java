package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductMonitoringService {
    /*private final ProductRepository productRepository;

    @Autowired
    public ProductMonitoringService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @PostConstruct
    @Transactional
    public void productMonitoring(){
        List<Product> productList = productRepository.findAll();
        Parser parser = new Parser();

        Runnable r = () -> {
            while (true) {
                for (int i = 0; i < productList.size(); i++) {
                    Product product = productList.get(i);
                    try {
                        product = parser.parseProduct(product);
                        productRepository.save(product);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (WebDriverException e) {
                        System.out.println("Товар с артикулом " + product.getArticle() + " на вайлдбериз отсутствует");
                    }
                }
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();

    }*/
}

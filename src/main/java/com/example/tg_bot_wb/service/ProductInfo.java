package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductInfo {

    private List<Product> updateProductList;
    @Autowired
    private final ProductRepository productRepository;

    public ProductInfo(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> parseProductInfo() {

        List<Product> productList = productRepository.findAll();
        List<Product> updateProductList = new ArrayList<>();
        Parser parser = new Parser();

        for(int i = 0; i < productList.size(); i++){
            Product product = productList.get(i);
//            try {
//               product =  parser.parseProduct(product);
                product.setPrice(product.getPrice()-500.0);
               productRepository.save(product);
               updateProductList.add(product);
           /* } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (WebDriverException e) {
                System.out.println("Товар с артикулом " + product.getArticle() + " на вайлдбериз отсутствует");
            }*/
        }
        return updateProductList;
    }
}

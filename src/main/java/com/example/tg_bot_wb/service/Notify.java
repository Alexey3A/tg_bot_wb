package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Notify {

    private final PersonService personService;
    private final ProductService productService;
    private final RequestDetailsService requestDetailsService;

    @Autowired
    public Notify(PersonService personService, ProductService productService, RequestDetailsService requestDetailsService) {
        this.personService = personService;
        this.productService = productService;
        this.requestDetailsService = requestDetailsService;
    }

    public Map<Person, String> notificationForPerson(){
        Map<Person, String> map = new HashMap<>();
        List<Person> personList = personService.findAllPerson();
        List<Product> productList = productService.findAllProduct();

        for (int i = 0; i < productList.size(); i++){
            double startPrice = 0;
            double currentPrice = 0;
            Product product = productList.get(i);
            currentPrice = product.getPrice();
            for (int k = 0; k < personList.size(); k++){
                Person person = personList.get(k);
                List<Message> messageList = person.getMessageList();
                for (int j = 0; j < messageList.size(); j++){
                    Message message = messageList.get(j);
                    RequestDetails requestDetails = message.getRequestDetails();
                    if (requestDetails.getProduct() == product.getId()) {
                        startPrice = requestDetails.getCurrentPrice();
                        if (currentPrice != startPrice) {
                            requestDetails.setStartPrice(startPrice);
                            requestDetails.setCurrentPrice(currentPrice);
                            requestDetailsService.saveRequestDetails(requestDetails);
                            System.out.println(product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                    + "изменение цены: " + startPrice + " -> " + currentPrice);
                            if(currentPrice != -1) {
                                map.put(person
                                        , product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                                + " \n" + "изменение цены: " + startPrice + " -> " + currentPrice);
                            } else {
                                map.put(person
                                        , product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                                + "\n" + "товара нет в наличии");
                            }
                        }
                    }
                }
            }
        }
        return map;
    }
}

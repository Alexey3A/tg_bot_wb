package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Notify {

    private final PersonService personService;
    private final ProductService productService;
    private final RequestDetailsService requestDetailsService;
    private final Bot bot;

    @Autowired
    public Notify(PersonService personService, ProductService productService, RequestDetailsService requestDetailsService, Bot bot) {
        this.personService = personService;
        this.productService = productService;
        this.requestDetailsService = requestDetailsService;
        this.bot = bot;
    }

    public void notificationForPerson(){
        List<Person> personList = personService.findAllPerson();
        List<Product> productList = productService.findAllProduct();

        for (Product product : productList) {
            double currentPrice = product.getPrice();
            for (Person person : personList) {
                List<Message> messageList = person.getMessageList();
                for (Message message : messageList) {
                    RequestDetails requestDetails = message.getRequestDetails();
                    if (requestDetails.getProduct() == product.getId()) {
                        double startPrice = requestDetails.getCurrentPrice();
                        if (currentPrice != startPrice) {
                            requestDetails.setStartPrice(startPrice);
                            requestDetails.setCurrentPrice(currentPrice);
                            requestDetailsService.saveRequestDetails(requestDetails);
                            System.out.println(product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                    + "изменение цены: " + startPrice + " -> " + currentPrice);
                            if (currentPrice != -1) {
                                String s = product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                        + " \n" + "изменение цены: " + startPrice + " -> " + currentPrice;
                                bot.sendText(person.getTgUserID(), s);
                            } else {
                                String s = product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                        + "\n" + "товара нет в наличии";
                                bot.sendText(person.getTgUserID(), s);
                            }
                        }
                    }
                }
            }
        }
    }
}

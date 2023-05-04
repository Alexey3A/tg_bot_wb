package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notify {
    @Autowired
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    @Autowired
    private final RequestDetailsRepository requestDetailsRepository;

    public Notify(PersonRepository personRepository, ProductRepository productRepository, RequestDetailsRepository requestDetailsRepository) {
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.requestDetailsRepository = requestDetailsRepository;
    }

    public Map<Person, String> notificationForPerson(){
        Map<Person, String> map = new HashMap<>();
        List<Person> personList = personRepository.findAll();
        List<Product> productList = productRepository.findAll();

        for (int i = 0; i < productList.size(); i++){
            double startPrice = 0;
            double currentPrice = 0;
            Product product = productList.get(i);
            currentPrice = product.getCurrentPrice();
            for (int k = 0; k < personList.size(); k++){
                Person person = personList.get(k);
                List<Message> messageList = person.getMessageList();
                for (int j = 0; j < messageList.size(); j++){
                    Message message = messageList.get(j);
                    RequestDetails requestDetails = message.getRequestDetails();
                    if (requestDetails.getProduct().getId() == product.getId()) {
                        startPrice = requestDetails.getStartPrice();
                        if (currentPrice != startPrice && currentPrice != requestDetails.getExpectedPrice()) {
                            requestDetails.setExpectedPrice(currentPrice);
                            requestDetailsRepository.save(requestDetails);
                            System.out.println(product.getProductName()
                                    + "  " + "изменение цены: " + startPrice + " -> " + currentPrice);
                            map.put(person
                                    , product.getProductName()
                                            + " \n" + "изменение цены: " + startPrice + " -> " + currentPrice);
                        }
                    }
                }
            }
        }
        return map;
    }
}

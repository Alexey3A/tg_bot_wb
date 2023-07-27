package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MessageDAOImpl implements MessageDAO{
    @Autowired
    EntityManager entityManager;
    @Override
    @Transactional
    public void saveOrUpdateMessage(Person person, Message personMessage, Product product) {
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setStartPrice(product.getCurrentPrice());
        requestDetails.setProduct(product);

        person.addProductToPerson(product);

        personMessage.setPerson(person);
        personMessage.setRequestDetails(requestDetails);

        entityManager.merge(personMessage);
    }
}

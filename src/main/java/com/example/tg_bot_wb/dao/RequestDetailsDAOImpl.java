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
public class RequestDetailsDAOImpl implements RequestDetailsDAO {
    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void saveOrUpdateRequestDetails(Person person, Message personMessage, Product product) {
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setProduct(product);
        requestDetails.setStartPrice(product.getCurrentPrice());
        person.addMessageToPerson(personMessage);
        person.addProductToPerson(product);
//        requestDetails.setMessage(personMessage);
        personMessage.setRequestDetails(requestDetails);
//        personMessage.setPerson(person);

        entityManager.merge(requestDetails);
    }

    @Override
    @Transactional
    public void saveOrUpdateRequestDetails(Product product) {

        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setProduct(product);

        entityManager.merge(requestDetails);
    }
}

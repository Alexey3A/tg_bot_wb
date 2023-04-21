package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestDetailsDAOImpl implements RequestDetailsDAO {
    @Autowired
    EntityManager entityManager;

    @Override
    public void saveOrUpdateRequestDetails(Person person, Message personMessage, Product product) {

    }
}

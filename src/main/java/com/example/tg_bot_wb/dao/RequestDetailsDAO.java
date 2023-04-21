package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;

public interface RequestDetailsDAO {
    void saveOrUpdateRequestDetails(Person person, Message personMessage, Product product);
}

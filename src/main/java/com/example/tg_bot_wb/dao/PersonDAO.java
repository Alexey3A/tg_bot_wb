package com.example.tg_bot_wb.dao;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;

import java.util.List;

public interface PersonDAO {
    void saveOrUpdatePerson(Person person);
    void saveOrUpdatePerson(Person person, Message personMessage);
    void saveOrUpdatePerson(Person person, Message personMessage, Product product);
    void deletePerson(long id);
    Person getPerson(long id);
    List<Person> getAllPerson();
}

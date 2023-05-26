package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PersonService {
    Person savePerson(Person Person);
    List<Person> findAllPerson();
}

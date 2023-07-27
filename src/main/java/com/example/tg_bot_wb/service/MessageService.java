package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;

import java.util.List;

public interface MessageService {
    List<Message> findAllByPerson(Person person);
}

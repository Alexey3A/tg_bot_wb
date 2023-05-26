package com.example.tg_bot_wb.repository;

import com.example.tg_bot_wb.entity.Message;
import com.example.tg_bot_wb.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByPerson(Person person);
 //   @Query("SELECT m FROM Message m WHERE m.message_text = ?1 and m.person_id = ?2")
    List<Message> findAllByMessageTextAndPerson(String messageText, Person person);
}

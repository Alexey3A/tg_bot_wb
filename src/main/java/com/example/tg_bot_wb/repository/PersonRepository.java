package com.example.tg_bot_wb.repository;

import com.example.tg_bot_wb.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByTgUserID(Long id);
}

package com.example.tg_bot_wb.repository;

import com.example.tg_bot_wb.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByTgUserID(Long id);
}

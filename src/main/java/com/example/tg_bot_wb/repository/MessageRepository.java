package com.example.tg_bot_wb.repository;

import com.example.tg_bot_wb.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

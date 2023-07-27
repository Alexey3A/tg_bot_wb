package com.example.tg_bot_wb;

import com.example.tg_bot_wb.service.Bot;
import com.example.tg_bot_wb.service.PersonService;
import com.example.tg_bot_wb.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@SpringBootApplication
public class TgBotWbApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgBotWbApplication.class, args);
    }

    @Bean
    public Bot getUser(@Value("${botToken}") String botToken, PersonService personService
            , ProductService productService) throws TelegramApiException {

        return new Bot(botToken, personService, productService);
    }
}



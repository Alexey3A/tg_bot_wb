package com.example.tg_bot_wb;

import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import com.example.tg_bot_wb.service.Bot;
import com.example.tg_bot_wb.service.PersonService;
import com.example.tg_bot_wb.service.ProductService;
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
    public Bot getUser(PersonService personService
            , ProductService productService) throws TelegramApiException {

        Bot bot = new Bot("6097077392:AAG4hsSfzcfXrRfFsL3INFCi-dehcHpH-EY"
                , personService
                , productService);

        return bot;
    }
}



package com.example.tg_bot_wb;

import com.example.tg_bot_wb.dao.PersonDAO;
import com.example.tg_bot_wb.dao.ProductDAO;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TgBotWbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgBotWbApplication.class, args);
	}

	@Bean
	public Person getUser(PersonRepository personRepository
						, ProductRepository productRepository
						, MessageRepository messageRepository
						, RequestDetailsRepository requestDetailsRepository
						, ProductDAO productDAO, PersonDAO personDAO) throws TelegramApiException {

		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		Bot bot = new Bot("*****************************"
							,personRepository, productRepository
							, messageRepository, requestDetailsRepository
							, personDAO, productDAO);
		botsApi.registerBot(bot);
		bot.sendText(5124083894L,"Hi!");

		return new Person();
	}
}

package com.example.tg_bot_wb;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.repository.PersonRepository;
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
	public Person getUser(PersonRepository personRepository) throws TelegramApiException {

//		Person person = new Person("Fedor");
//		personRepository.save(person);

		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		Bot bot = new Bot(personRepository);
		botsApi.registerBot(bot);
		bot.sendText(5124083894L,"Hi!");

		return new Person();
	}
}

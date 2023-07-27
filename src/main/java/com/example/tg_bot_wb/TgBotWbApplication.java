package com.example.tg_bot_wb;

import com.example.tg_bot_wb.dao.MessageDAO;
import com.example.tg_bot_wb.dao.PersonDAO;
import com.example.tg_bot_wb.dao.ProductDAO;
import com.example.tg_bot_wb.dao.RequestDetailsDAO;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import com.example.tg_bot_wb.service.Bot;
import com.example.tg_bot_wb.service.Notify;
import com.example.tg_bot_wb.service.ProductInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TgBotWbApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgBotWbApplication.class, args);
    }

    @Bean
    public Bot getUser(PersonRepository personRepository
            , ProductRepository productRepository
            , MessageRepository messageRepository
            , RequestDetailsRepository requestDetailsRepository
            , ProductDAO productDAO, PersonDAO personDAO
            , RequestDetailsDAO requestDetailsDAO
            , MessageDAO messageDAO) throws TelegramApiException {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot("6097077392:AAG4hsSfzcfXrRfFsL3INFCi-dehcHpH-EY"
                , personRepository, productRepository
                , messageRepository, requestDetailsRepository
                , personDAO, productDAO, requestDetailsDAO
                , messageDAO);
        botsApi.registerBot(bot);
        bot.sendText(5124083894L, "Hi!");

        Runnable r = () -> {
            while (true) {
                Notify notify = new Notify(personRepository, productRepository, requestDetailsRepository);
                Map<Person, String> messageForPerson = notify.notificationForPerson();
                bot.sendAPriceChangeNotification(messageForPerson);

                System.out.println("privet");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        return bot;
    }


	/*@Bean
	public Person getUser(ProductRepository productRepository) {

		List<Product> productList  = new ProductInfo(productRepository).parseProductInfo();
		System.out.println(productList);

		return new Person();
	}*/
}

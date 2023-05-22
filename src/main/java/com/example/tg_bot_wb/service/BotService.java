package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;

@Service
public class BotService {

    private  final Bot bot;
    private final PersonRepository personRepository;
    private final RequestDetailsRepository requestDetailsRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BotService(Bot bot, PersonRepository personRepository, RequestDetailsRepository requestDetailsRepository, ProductRepository productRepository) {
        this.bot = bot;
        this.personRepository = personRepository;
        this.requestDetailsRepository = requestDetailsRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        bot.sendText(8888888888888888L, "Hi!");

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

        Runnable r2 = () -> {
            while (true) {
                List<Product> productList = productRepository.findAll();
                for(Product product : productList) {
                    try {
                        product = new Parser(product).parseProduct(product);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    productRepository.save(product);
                }

                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(r);
        Thread thread2 = new Thread(r2);
        thread.start();
        thread2.start();
    }
}

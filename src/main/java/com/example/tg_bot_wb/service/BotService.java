package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriverException;
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
    private final ProductService productService;
    private final Notify notify;

    @Autowired
    public BotService(Bot bot, ProductService productService, Notify notify) {
        this.bot = bot;
        this.productService = productService;
        this.notify = notify;
    }

    @PostConstruct
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        bot.sendText(5124083894L, "Hi!");

        Runnable r = () -> {
            while (true) {
                Map<Person, String> messageForPerson = notify.notificationForPerson();
                bot.sendAPriceChangeNotification(messageForPerson);

                System.out.println("privet");

                try {
                    Thread.sleep(350000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable r2 = () -> {
            while (true) {
                List<Product> productList = productService.findAllProduct();
                for(Product product : productList) {
                    try {
                        product = new Parser(product).parseProduct(product);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    } catch (WebDriverException e2) {
                        System.out.println(e2.getMessage());
                    }
                    productService.saveProduct(product);
                }

                try {
                    Thread.sleep(1200000);
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




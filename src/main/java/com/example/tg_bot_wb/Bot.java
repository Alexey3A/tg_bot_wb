package com.example.tg_bot_wb;


import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.openqa.selenium.WebDriverException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {
    private PersonRepository personRepository;
    private ProductRepository productRepository;
    private MessageRepository messageRepository;
    private RequestDetailsRepository requestDetailsRepository;
    private boolean isArticle = false;

    public Bot(String botToken) {
        super(botToken);
    }

    public Bot(String botToken, PersonRepository personRepository
            , ProductRepository productRepository
            , MessageRepository messageRepository
            , RequestDetailsRepository requestDetailsRepository) {
        super(botToken);
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.messageRepository = messageRepository;
        this.requestDetailsRepository = requestDetailsRepository;
    }

    public String getBotUsername() {
        return "WbBot";
    }

    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        long userID = user.getId();

        System.out.println(update);
        System.out.println(user.getFirstName() + " wrote " + msg.getText());

        String article = msg.getText();
        System.out.println(article);

        Person person = personRepository.findByTgUserID(userID);

        if (person == null) {
            person = new Person(user.getFirstName(), userID);
            personRepository.save(person);
        }

        Product product = productRepository.findByArticle(article);

        if (isArticle) {

            if (product == null) {
                product = new Product(article);

                Parser parser = new Parser(product);

                try {
                    parser.parseProduct(product);
                    product = parser.getProduct();
                    product.addPersonToProduct(person);
                    productRepository.save(product);
                    sendText(userID, "Товар: " + product.getProductName());
                    sendText(userID, "Цена: " + product.getCurrentPrice() + " р.");
                } catch (IllegalArgumentException e) {
                    sendText(userID, "Укажите корректный артикул товара");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (WebDriverException e) {
                    sendText(userID, "Товар c артикулом " + product.getArticle() + " не найден");
                }
            } else {
                product.addPersonToProduct(person);
                sendText(userID, "Товар: " + product.getProductName());
                sendText(userID, "Цена: " + product.getCurrentPrice() + " р.");
            }
        }

        if (msg.isCommand()) {
            if (msg.getText().equals("/article")) {
                isArticle = true;
                sendText(userID, "Укажите артикул товара");
            }
        }

    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyMessage(Long who, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())
                .chatId(who.toString())
                .messageId(msgId)
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

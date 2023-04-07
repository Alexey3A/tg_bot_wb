package com.example.tg_bot_wb;


import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.repository.PersonRepository;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

    private PersonRepository personRepository;
    private boolean screaming = false;
    private boolean isArticle = false;


    public Bot() {
    }

    public Bot(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public String getBotUsername() {
        return "DemoBot";
    }

    public String getBotToken() {
        return "6097077392:AAFzXBt7XCXQmOpTCfGBK1jMjedHXD-I7SI";
    }

    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        long userID = user.getId();

//        sendText(userID, msg.getText());
//        copyMessage(userID, msg.getMessageId());

        System.out.println(update);
        System.out.println(user.getFirstName() + " wrote " + msg.getText());

/*        if(screaming) {
            scream(userID, msg);
        } else {
            copyMessage(userID, msg.getMessageId());
        }

        if(msg.isCommand()) {
            if(msg.getText().equals("/scream"))
                screaming = true;
            else if(msg.getText().equals("/whisper")) screaming = false;
        }

        if(msg.isCommand()) {
            if(msg.getText().equals("/start"))
                sendText(userID, "Hi, " + user.getFirstName());
        }*/

        if (isArticle) {
            String article = msg.getText();
            System.out.println(article);
            Product product = new Product(article);
            Parser parser = new Parser(product);
            try {
                parser.parseProduct(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e) {
                sendText(userID, "Товар c артикулом " + product.getArticle() + " не найден");
            } catch (WebDriverException e) {
                sendText(userID, "Что-то пошло не так...((");
            }
            sendText(userID, "Товар: " + product.getProductName());
            sendText(userID, "Цена: " + product.getCurrentPrice() + " р.");
        }

        if (msg.isCommand()) {
            if (msg.getText().equals("/article")) {
                isArticle = true;
                sendText(userID, "Укажите артикул товара");
            }
        }

        Person p = personRepository.findByTgUserID(userID);

        if (p == null) {
            Person person = new Person(user.getFirstName(), userID);
            personRepository.save(person);
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

    private void scream(Long id, Message msg) {
        if (msg.hasText()) {
            sendText(id, msg.getText().toUpperCase());
        } else {
            copyMessage(id, msg.getMessageId());
        }
    }
}

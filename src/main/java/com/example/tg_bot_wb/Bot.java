package com.example.tg_bot_wb;


import com.example.tg_bot_wb.dao.PersonDAO;
import com.example.tg_bot_wb.dao.ProductDAO;
import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.openqa.selenium.WebDriverException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    private PersonRepository personRepository;
    private ProductRepository productRepository;
    private MessageRepository messageRepository;
    private RequestDetailsRepository requestDetailsRepository;
    private PersonDAO personDAO;
    private ProductDAO productDAO;
    private boolean isArticle = false;
    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;

    public Bot(String botToken) {
        super(botToken);
    }

    public Bot(String botToken, PersonRepository personRepository
            , ProductRepository productRepository
            , MessageRepository messageRepository
            , RequestDetailsRepository requestDetailsRepository
            , PersonDAO personDAO, ProductDAO productDAO) {
        super(botToken);
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.messageRepository = messageRepository;
        this.requestDetailsRepository = requestDetailsRepository;
        this.personDAO = personDAO;
        this.productDAO = productDAO;
    }

    public String getBotUsername() {
        return "WbBot";
    }

    public void onUpdateReceived(Update update) {

        User user = null;
        Message msg = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            msg = update.getMessage();
            user = msg.getFrom();
        }
        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Back").callbackData("back")
                .build();

        var url = InlineKeyboardButton.builder()
                .text("Tutorial")
                .url("https://core.telegram.org/bots/api")
                .build();

        keyboardM1 = InlineKeyboardMarkup.builder().keyboardRow(List.of(next)).build();
        keyboardM2 = InlineKeyboardMarkup.builder().keyboardRow(List.of(back)).keyboardRow(List.of(url)).build();


//        long userID = user.getId();

        /*System.out.println(update);
        System.out.println(user.getFirstName() + " wrote " + msg.getText());

        String article = msg.getText();
        System.out.println(article);

        Message personMessage = new Message(msg.getText(), System.currentTimeMillis());
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
                    personDAO.saveOrUpdatePerson(person, personMessage, product);
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
                person.addProductToPerson(product);
                RequestDetails requestDetails = new RequestDetails();
                requestDetails.setProduct(product);
                requestDetails.setStartPrice(product.getCurrentPrice());
                requestDetails.setMessage(personMessage);
                personMessage.setRequestDetails(requestDetails);
                personMessage.setPerson(person);
                person.addMessageToPerson(personMessage);

               requestDetailsRepository.save(requestDetails);

                sendText(userID, "Товар: " + product.getProductName());
                sendText(userID, "Цена: " + product.getCurrentPrice() + " р.");
            }
        }*/


        if (msg != null && msg.isCommand()) {
            var txt = msg.getText();

            if (txt.equals("/article")) {
                isArticle = true;
                sendText(msg.getFrom().getId(), "Укажите артикул товара");
            } else if (txt.equals("/menu")) {
//                sendMenu(user.getId(), "<b>Menu 1</b>", keyboardM1);
                sendMenu(msg);
                isArticle = false;
            }
        }
        if (msg != null && msg.getText().equals("Мой список товаров")) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(user.getId().toString())
                    .text("Тут будет список выших товаров").build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException();
            }
        }

        if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();

            Long id = callbackQuery.getMessage().getChatId();
            String queryId = callbackQuery.getId();
            String data = callbackQuery.getData();
            int msgId = callbackQuery.getMessage().getMessageId();

            try {
                buttonTap(callbackQuery);
//                buttonTap(id, queryId, data, msgId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();

        if (data.equals("next")) {
            newTxt.setText("MENU 2");
            newKb.setReplyMarkup(keyboardM2);
        } else if (data.equals("back")) {
            newTxt.setText("MENU 1");
            newKb.setReplyMarkup(keyboardM1);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }

    private void buttonTap(CallbackQuery callbackQuery) throws TelegramApiException {
        Long id = callbackQuery.getMessage().getChatId();
        String queryId = callbackQuery.getId();
        String data = callbackQuery.getData();
        int msgId = callbackQuery.getMessage().getMessageId();

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        if (data.equals("Мой список товаров")) {
            newTxt.setText("MENU 2");
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);

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

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Message msg) {

        // Создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new
                ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add("Мой список товаров");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("Пока просто кнопка");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанавливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = SendMessage.builder()
                .replyMarkup(replyKeyboardMarkup)
                .chatId(msg.getChatId())
                .replyToMessageId(msg.getMessageId())
                .text("-")
                .build();

        try {
            execute(sendMessage);
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

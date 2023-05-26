package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.entity.RequestDetails;
import com.example.tg_bot_wb.exeption.ProductAbsenceException;
import com.example.tg_bot_wb.repository.MessageRepository;
import com.example.tg_bot_wb.repository.PersonRepository;
import com.example.tg_bot_wb.repository.ProductRepository;
import com.example.tg_bot_wb.repository.RequestDetailsRepository;
import org.openqa.selenium.WebDriverException;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class Bot extends TelegramLongPollingBot {
    private PersonRepository personRepository;
    private ProductRepository productRepository;
    private MessageRepository messageRepository;
    private RequestDetailsRepository requestDetailsRepository;
    private ProductService productService;
    private boolean isArticle = false;
    private boolean isDeleteArticle = false;

    public Bot(String botToken) {
        super(botToken);
    }

    public Bot(String botToken, PersonRepository personRepository
            , ProductRepository productRepository
            , MessageRepository messageRepository
            , RequestDetailsRepository requestDetailsRepository
            , ProductService productService) {
        super(botToken);
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.messageRepository = messageRepository;
        this.requestDetailsRepository = requestDetailsRepository;
        this.productService = productService;
    }

    public String getBotUsername() {
        return "WbBot";
    }

    @Transactional
    public void onUpdateReceived(Update update) {

        User user = null;
        Message msg = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            msg = update.getMessage();
            user = msg.getFrom();
        }

        long tgUserID = user.getId();

        System.out.println(update);
        String article = msg.getText();
        System.out.println(user.getFirstName() + " wrote " + article);


        if (!isArticle && !article.equals("Добавить товар")
                && !article.equals("Мой список товаров")
                && !article.equals("Удалить товар")) {
            sendText(tgUserID, "Выберите из меню, что нужно сделать");
        }

        com.example.tg_bot_wb.entity.Message personMessage = new com.example.tg_bot_wb.entity.Message(article, System.currentTimeMillis());
        Person person = personRepository.findByTgUserID(tgUserID);

        if (person == null) {
            person = new Person(user.getFirstName(), tgUserID);
            person = personRepository.save(person);
        }
        Product product = productRepository.findByArticle(article);

        if (msg.isCommand()) {
            var txt = msg.getText();
            if (txt.equals("/menu")) {
                isArticle = false;
                sendMenu(msg);
            }
            return;
        }

        if (article.equals("Добавить товар") || article.equals("Удалить товар")) {
            isArticle = true;
            if (article.equals("Удалить товар")) isDeleteArticle = true;
            sendText(tgUserID, "Укажите артикул товара");

        }

        if (article.equals("Мой список товаров")) {
            isArticle = false;
            String productList = person.getProductList().toString();
            if (productList != null) {
                sendText(tgUserID, productList);
            } else {
                sendText(tgUserID, "Список пустой. Нажмите кнопку \"Добавить товар\"");
            }
        }

        if (isArticle && !isDeleteArticle
                && !article.equals("Добавить товар")) {

            if (product == null) {
                product = new Product(article);
                Parser parser = new Parser(product);
                try {
                    parser.parseProduct(product);
                    product = parser.getProduct();
                    productService.saveProduct(product, person, personMessage);
                    if (product.getPrice() != -1.0) {
                        sendText(tgUserID, "Добавлен товар: " + product.getProductName() + " (артикул: " + product.getArticle()
                                + "\n" + "Цена: " + product.getPrice() + " р.");
                    } else {
                        sendText(tgUserID, "Добавлен товар: " + product.getProductName() + " (артикул: " + product.getArticle() + ") "
                                + "\n" + "Цена: товара нет в наличии");
                    }
                } catch (IllegalArgumentException e) {
                    sendText(tgUserID, "Укажите корректный артикул товара");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (WebDriverException e) {
                    sendText(tgUserID, "Товар c артикулом " + product.getArticle() + " не найден");
                }
            } else {
                product = productService.saveProduct(product, person, personMessage);
                if (product.getPrice() != -1.0) {
                    sendText(tgUserID, "Добавлен товар: " + product.getProductName() + " (артикул: " + product.getArticle()
                            + "\n" + "Цена: " + product.getPrice() + " р.");
                } else {
                    sendText(tgUserID, "Добавлен товар: " + product.getProductName() + " (артикул: " + product.getArticle() + ") "
                            + "\n" + "Цена: товара нет в наличии");
                }
            }
            isArticle = false;
        }

        if (isDeleteArticle && isArticle && !article.equals("Удалить товар")) {
            try {
                productService.delete(person, article);
                sendText(tgUserID, "Удален: " + product);
                System.out.println("Должно было произойти удаление товара у пользователя");
            } catch (ProductAbsenceException e) {
                System.out.println(e.getMessage());
                sendText(tgUserID, "Товар с артикулом " + article + " в вашем списке отсутствует");
            }
            isDeleteArticle = false;
            isArticle = false;
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            try {
                buttonTap(callbackQuery);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
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
        keyboardSecondRow.add("Добавить товар");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Добавляем кнопки в третью строчку клавиатуры
        keyboardSecondRow.add("Удалить товар");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
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

    public void sendAPriceChangeNotification(Map<Person, String> messageForPerson) {
        Set<Map.Entry<Person, String>> set = messageForPerson.entrySet();
        for (Map.Entry<Person, String> entry : set) {
            Person person = entry.getKey();
            String message = entry.getValue();
            sendText(person.getTgUserID(), message);
        }
    }
}

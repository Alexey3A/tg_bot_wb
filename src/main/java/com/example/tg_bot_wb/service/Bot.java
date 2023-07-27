package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Person;
import com.example.tg_bot_wb.entity.Product;
import com.example.tg_bot_wb.exсeption.ProductAbsenceException;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class Bot extends TelegramLongPollingBot {

    private final PersonService personService;
    private final ProductService productService;
    private boolean isArticle = false;
    private boolean isDeleteArticle = false;
    private boolean isAdmin = false;
    @Value("${bot.adminId}")
    private long  adminId;
    private static final String infoCommand = "Бот предоставляет информацию о стоимости товаров без учета вашей индивидуальной скидки на Wildberries. \n"
              + "Чтобы добавить товар к списку интересующих вас товаров, нажмите кнопку \"Добавить товар\" и укажите артикул товара. Если цена товара измениться, то бот пришлет вам уведомление. \n"
              + "Все сведения, предоставляемые ботом, носят информационный характер.\n Возможна рассылка рекламы.";

    @Autowired
    public Bot(String botToken, PersonService personService, ProductService productService) {
        super(botToken);
        this.personService = personService;
        this.productService = productService;
    }

    public String getBotUsername() {
        return "WbBot";
    }

    public long getAdminId() {
        return adminId;
    }

    @Transactional
    public void onUpdateReceived(Update update) {

        User user = null;
        Message msg = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            msg = update.getMessage();
            user = msg.getFrom();
        }

        assert user != null;
        long tgUserID = user.getId();

        System.out.println(update);
        String article = msg.getText();
        System.out.println(user.getFirstName() + " wrote " + article);

        if (!isArticle && !article.equals("Добавить товар")
                && !article.equals("Мой список товаров")
                && !article.equals("Удалить товар")
                && !article.equals("сообщение для всех")) {
            sendText(tgUserID, "Выберите из меню, что нужно сделать");
        }

        com.example.tg_bot_wb.entity.Message personMessage = new com.example.tg_bot_wb.entity.Message(article, System.currentTimeMillis());
        Person person = personService.findByTgUserID(tgUserID);

        if (person == null) {
            person = new Person(user.getFirstName(), tgUserID);
            person = personService.savePerson(person);
        }
        Product product = productService.findByArticle(article);

        if (msg.isCommand()) {
            var txt = msg.getText();
            if (txt.equals("/menu")) {
                isArticle = false;
                sendMenu(msg);
            } else if (txt.equals("/info")){
                sendText(tgUserID, infoCommand);
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

        // Сообщение от администратора для всех пользователей
        if (article.equals("сообщение для всех") && tgUserID ==  adminId) {
            isAdmin = true;
            sendText(tgUserID, "Напишите сообщение для всех");
        }

        if (!article.equals("сообщение для всех") && tgUserID == adminId && isAdmin) {
            for (Person p : personService.findAllPerson()) {
                sendText(p.getTgUserID(), article);
            }
            isAdmin = false;
        }

        if (isArticle && !isDeleteArticle
                && !article.equals("Добавить товар")
                && !isAdmin) {
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
                productService.deleteProductFromPerson(person, article);
                productService.deleteProduct(article);
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
        keyboardSecondRow.add("Удалить товар");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Добавляем кнопки в третью строчку клавиатуры
        keyboardThirdRow.add("сообщение для всех");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // кнопка администратора для отправления сообщения всем пользователям
        if (msg.getFrom().getId() == adminId) {
            keyboard.add(keyboardThirdRow);
        }

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

    public void sendAPriceChangeNotification(Map<Person, List<String>> messageForPerson) {
        Set<Map.Entry<Person, List<String>>> set = messageForPerson.entrySet();
        for (Map.Entry<Person, List<String>> entry : set) {
            Person person = entry.getKey();
            List<String> messages = entry.getValue();
            for (String message : messages){
                sendText(person.getTgUserID(), message);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.example.tg_bot_wb;

import com.example.tg_bot_wb.entity.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class Parser {

    private final String URL = "https://www.wildberries.ru/";

    private Product product;

    public Parser() {
    }

    public Parser(Product product) {
        this.product = product;
    }

    public Product parseProduct(Product product) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/IdeaProjects/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        String article = product.getArticle();
        driver.get(URL);

        Thread.sleep(2000);

        driver.findElement(new By.ByXPath("/html/body/div[1]/header/div/div[2]/div[3]/div[1]/input"))
                .sendKeys(article);
        driver.findElement(By.id("applySearchBtn")).click();

        Thread.sleep(4000);

        String productName = driver.findElement(By.tagName("h1")).getText();
        product.setProductName(productName);

        String price = driver.findElement(By
                        .className("product-page__aside-sticky")).findElement(By.tagName("p"))
                .getText();

        price = price.replaceAll("\\s", "");
        price = price.substring(0, price.indexOf("₽"));
        double doublePrice = Double.parseDouble(price);
        System.out.println(price);
        product.setCurrentPrice(doublePrice);

        driver.quit();
        return product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

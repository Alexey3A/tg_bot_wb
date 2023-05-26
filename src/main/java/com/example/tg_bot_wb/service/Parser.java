package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Parser {

    private final String URL = "https://www.wildberries.ru/";

    private Product product;

    public Parser() {
    }

    public Parser(Product product) {
        this.product = product;
    }

    public Product parseProduct(Product product) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C://IdeaProjects/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        String article = product.getArticle();
        driver.get(URL);


        try {
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

            Thread.sleep(1000);

            price = price.replaceAll("\\s", "");
            try{
                price = price.substring(0, price.indexOf("₽"));
            } catch (StringIndexOutOfBoundsException e) {       // если товара нет в наличии, то ему присваевается цена "-1"
                price = "-1";
            }
            double doublePrice = Double.parseDouble(price);
            System.out.println(price);
            product.setPrice(doublePrice);
        } catch (IllegalArgumentException e){
            driver.quit();
            throw new IllegalArgumentException();
        } catch (InterruptedException e) {
            driver.quit();
            throw new InterruptedException();
        } catch (WebDriverException e) {
            driver.quit();
            throw new WebDriverException();
        }


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

package com.example.tg_bot_wb.service;

import com.example.tg_bot_wb.entity.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Parser {

    private final String URL = "https://www.wildberries.ru/";

    private Product product;

    public Parser() {
    }

    public Parser(Product product) {
        this.product = product;
    }

    public Product parseProduct(Product product) throws InterruptedException {

            /*System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);*/

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setBrowserName("chrome");
        dc.setCapability(ChromeOptions.CAPABILITY, options);
        java.net.URL url = null;
        try {
            url = new URL("http://selenium-hub:4444/wd/hub");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        WebDriver driver = new RemoteWebDriver(url, dc);

        driver.manage().window().maximize();

        String article = product.getArticle();
        driver.get(URL);

        try {
            Thread.sleep(1000);
            driver.findElement(new By.ByXPath("/html/body/div[1]/header/div/div[2]/div[3]/div[1]/input"))
                    .sendKeys(article);
            driver.findElement(By.id("applySearchBtn")).click();

            Thread.sleep(1000);

            String productName = driver.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div[3]/div/div[3]/div[5]/div[1]/h1"))
//                    driver.findElement(By.tagName("h1"))
                    .getText();

            System.out.println(productName);

            product.setProductName(productName);

            String price;

            try {
                price = driver.findElement(By
//                                                       .className("product-page__aside-sticky")).findElement(By.tagName("p"))
                                .xpath("/html/body/div[1]/main/div[2]/div/div[3]/div/div[3]/div[2]/div/div/div/p/span"))
                        .getText();
            } catch (WebDriverException e) {
                price = driver.findElement(By
//                                                       .className("product-page__aside-sticky")).findElement(By.tagName("p"))
                                .xpath("/html/body/div[1]/main/div[2]/div/div[3]/div/div[3]/div[2]/div/p/span"))
                        .getText();
            }

            System.out.println(price);

            Thread.sleep(1000);

            price = price.replaceAll("\\s", "");
            try {
                price = price.substring(0, price.indexOf("₽"));
            } catch (
                    StringIndexOutOfBoundsException e) {       // если товара нет в наличии, то ему присваевается цена "-1"
                price = "-1";
            }
            double doublePrice = Double.parseDouble(price);
            System.out.println(price);
            product.setPrice(doublePrice);
        } catch (IllegalArgumentException e) {
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

package com.example.tg_bot_wb.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "article")
    private String article;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "category")
    private String category;
    @Column(name = "current_price")
    private double currentPrice;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_person"
            , joinColumns = @JoinColumn(name = "product_id")
            , inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    public Product() {
    }

    public Product(String article) {
        this.article = article;
    }

    public Product(String article, String productName) {
        this.article = article;
        this.productName = productName;
    }

    public void addPersonToProduct(Person person){
        if(personList == null) {
            personList = new ArrayList<>();
        }
        personList.add(person);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}

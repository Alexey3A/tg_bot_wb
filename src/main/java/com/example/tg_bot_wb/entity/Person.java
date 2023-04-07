package com.example.tg_bot_wb.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "tg_user_id")
    private Long tgUserID;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name =  "is_bot")
    private boolean isBot;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Message> messageList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_person"
            , joinColumns = @JoinColumn(name = "person_id")
            , inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;

    public Person() {
    }

    public Person(String firstName, Long tgUserID) {
        this.firstName = firstName;
        this.tgUserID = tgUserID;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getTgUserID() {
        return tgUserID;
    }

    public void setTgUserID(Long tgUserID) {
        this.tgUserID = tgUserID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}

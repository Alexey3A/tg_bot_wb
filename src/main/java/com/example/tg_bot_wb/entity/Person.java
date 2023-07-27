package com.example.tg_bot_wb.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "person")
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
    @Column(name = "is_bot")
    private boolean isBot;

    @OneToMany(mappedBy = "person", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    private List<Message> messageList;

    //    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "personList")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_person"
            , joinColumns = @JoinColumn(name = "person_id")
            , inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> productList;

    public Person() {
    }

    public Person(String firstName, Long tgUserID) {
        this.firstName = firstName;
        this.tgUserID = tgUserID;
    }

    public void addProductToPerson(Product product) {
        if (productList.isEmpty()) {
            productList = new HashSet<>();
        }
        productList.add(product);
    }

    public void addMessageToPerson(Message message) {
        if (messageList.isEmpty()) {
            messageList = new ArrayList<>();
        }
        messageList.add(message);
        message.setPerson(this);
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

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return tgUserID.equals(person.tgUserID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgUserID);
    }


}

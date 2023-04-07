package com.example.tg_bot_wb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "date")
    private long date;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Message() {
    }

    public Message(long id, String messageText, long date) {
        this.id = id;
        this.messageText = messageText;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                ", date=" + date +
                '}';
    }
}

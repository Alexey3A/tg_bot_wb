package com.example.tg_bot_wb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "request_details")
public class RequestDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_price")
    private double startPrice;

    @Column(name = "expected_price")
    private double expectedPrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    Product product;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    Message message;

    public RequestDetails() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

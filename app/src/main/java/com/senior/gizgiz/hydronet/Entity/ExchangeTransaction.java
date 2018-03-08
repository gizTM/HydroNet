package com.senior.gizgiz.hydronet.Entity;

import java.util.Date;

/**
 * Created by Admins on 002 02/03/2018.
 */

public class ExchangeTransaction {
    private String id;
    private int exchangePlantCount;
    private UserPlant exchangePlant;
    private User seller,buyer;
    private float exchangeCondition;
    private Date exchangeTime;

    public ExchangeTransaction() { }

    public ExchangeTransaction(String id, UserPlant exchangePlant, int count, User seller, User buyer, float exchangeCondition, Date exchangeTime) {
        this.id = id;
        this.exchangePlant = exchangePlant;
        this.exchangePlantCount = count;
        this.seller = seller;
        this.buyer = buyer;
        this.exchangeCondition = exchangeCondition;
        this.exchangeTime = exchangeTime;
    }

    public String getId() { return id; }
    public int getExchangePlantCount() { return exchangePlantCount; }
    public UserPlant getExchangePlant() { return exchangePlant; }
    public User getSeller() { return seller; }
    public User getBuyer() { return buyer; }
    public float getExchangeCondition() { return exchangeCondition; }
    public Date getExchangeTime() { return exchangeTime; }

    public void setId(String id) { this.id = id; }
    public void setExchangePlantCount(int count) { this.exchangePlantCount = count; }
    public void setExchangePlant(UserPlant exchangePlant) { this.exchangePlant = exchangePlant; }
    public void setSeller(User seller) { this.seller = seller; }
    public void setBuyer(User buyer) { this.buyer = buyer; }
    public void setExchangeCondition(float exchangeCondition) { this.exchangeCondition = exchangeCondition; }
    public void setExchangeTime(Date exchangeTime) { this.exchangeTime = exchangeTime; }
}

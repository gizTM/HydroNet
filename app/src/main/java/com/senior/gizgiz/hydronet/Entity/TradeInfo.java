package com.senior.gizgiz.hydronet.Entity;

import java.util.List;

/**
 * Created by Admins on 005 05/03/2018.
 */

public class TradeInfo {
    private String id;
    private UserPlant userPlant;
    private List<ExchangeTransaction> userTransaction;
    private List<ProductAnnouncementStory> userSaleStory;

    public TradeInfo(String id, UserPlant userPlant, List<ExchangeTransaction> userTransaction, List<ProductAnnouncementStory> userSaleStory) {
        this.id = id;
        this.userPlant = userPlant;
        this.userTransaction = userTransaction;
        this.userSaleStory = userSaleStory;
    }

    public String getId() { return id; }
    public UserPlant getUserPlant() { return userPlant; }
    public List<ExchangeTransaction> getUserTransaction() { return userTransaction; }
    public List<ProductAnnouncementStory> getUserSaleStory() { return userSaleStory; }

    public void setId(String id) { this.id = id; }
    public void setUserPlant(UserPlant userPlant) { this.userPlant = userPlant; }
    public void setUserTransaction(List<ExchangeTransaction> userTransaction) { this.userTransaction = userTransaction; }
    public void setUserSaleStory(List<ProductAnnouncementStory> userSaleStory) { this.userSaleStory = userSaleStory; }
}

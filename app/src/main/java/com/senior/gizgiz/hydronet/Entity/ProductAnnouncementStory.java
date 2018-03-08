package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProductAnnouncementStory extends Story {
    public static final int COND_TYPE_MONEY = 0;
    public static final int COND_TYPE_OTHER = 1;

    private String saleStatus;
    private int exchangeConditionType;
    private String condition;
    private List<Negotiation> negotiations;
    private int historyNumber;

    public ProductAnnouncementStory() { }
    public ProductAnnouncementStory(User owner, int exchangeConditionType, String condition, String remark, UserPlant mentionedPlant, int historyNumber) {
        super(owner, StoryAdapter.getTypeSale(),remark,mentionedPlant);
        this.exchangeConditionType = exchangeConditionType;
        if(exchangeConditionType==COND_TYPE_MONEY) {
            float temp = 0;
            try {
                temp = new DecimalFormat("#").parse(condition).floatValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.condition = String.valueOf(new DecimalFormat("฿###,###.##").format(temp));
        }
        else this.condition = condition;
        this.saleStatus = "open";
        this.negotiations = new ArrayList<>();
        this.historyNumber = historyNumber;
    }

    public Map<String,Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("status",saleStatus);
        result.put("exchangetype",exchangeConditionType);
        result.put("condition",condition);
        result.put("historynumber",historyNumber);
        return result;
    }

    public String getSaleStatus() { return saleStatus; }
    public int getExchangeConditionType() { return exchangeConditionType; }
    public String getCondition() { return condition; }
    public List<Negotiation> getNegotiations() { return negotiations; }
    public int getHistoryNumber() { return historyNumber; }

    public void setSaleStatus(String saleStatus) { this.saleStatus = saleStatus; }
    public void setExchangeConditionType(int exchangeConditionType) { this.exchangeConditionType = exchangeConditionType; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setHistoryNumber(int historyNumber) { this.historyNumber = historyNumber; }
    public void addNegotiations(Negotiation negotiation) { this.negotiations.add(negotiation); }
}

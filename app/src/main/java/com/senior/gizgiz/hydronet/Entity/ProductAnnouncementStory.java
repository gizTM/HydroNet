package com.senior.gizgiz.hydronet.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProductAnnouncementStory extends Story {
    public static final int COND_TYPE_MONEY = 0;
    public static final int COND_TYPE_OTHER = 1;

    private String dueDate;
    private String saleStatus;
    private int exchangeConditionType,reserved=0;
    private String condition;
    private List<Negotiation> negotiations = new ArrayList<>();
    private Map<String,Boolean> negotiation = new HashMap<>(), reservedUsers = new HashMap<>();
    private int historyNumber;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProductAnnouncementStory createFromParcel(Parcel in) {
            return new ProductAnnouncementStory(in);
        }

        public ProductAnnouncementStory[] newArray(int size) {
            return new ProductAnnouncementStory[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(dueDate);
        dest.writeString(saleStatus);
        dest.writeInt(exchangeConditionType);
        dest.writeInt(reserved);
        dest.writeString(condition);
        dest.writeList(negotiations);
        dest.writeMap(negotiation);
        dest.writeInt(historyNumber);
    }

    public ProductAnnouncementStory(Parcel in) {
        super(in);
        this.dueDate = in.readString();
        this.saleStatus = in.readString();
        this.exchangeConditionType = in.readInt();
        this.reserved = in.readInt();
        this.condition = in.readString();
        this.negotiations = new ArrayList<>();
        in.readList(negotiations,Negotiation.class.getClassLoader());
        this.negotiation = new HashMap<>();
        in.readMap(negotiation,HashMap.class.getClassLoader());
        this.historyNumber = in.readInt();
    }

    public ProductAnnouncementStory() {}

    public ProductAnnouncementStory(int exchangeConditionType, String condition, String remark, int historyNumber,String postDate) {
        super(StoryAdapter.getTypeSale(),remark,postDate);
        reserved = 0;
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

    public ProductAnnouncementStory(User owner, int exchangeConditionType, String condition, String remark,
                                    int historyNumber, String dueDate, String postDate) {
        super(owner, StoryAdapter.getTypeSale(),remark,postDate);
        reserved = 0;
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
        this.dueDate = dueDate;
    }

    public ProductAnnouncementStory(User owner, int exchangeConditionType, String condition, String remark, UserPlant mentionedPlant, int historyNumber,String postDate) {
        super(owner, StoryAdapter.getTypeSale(),remark,mentionedPlant,postDate);
        reserved = 0;
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
    public int getReserved() { return reserved; }
    public Map<String, Boolean> getNegotiation() { return negotiation; }
    public Map<String, Boolean> getReservedUsers() { return reservedUsers; }
    public String getDueDate() { return dueDate; }

    public void setSaleStatus(String saleStatus) { this.saleStatus = saleStatus; }
    public void setExchangeConditionType(int exchangeConditionType) { this.exchangeConditionType = exchangeConditionType; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setHistoryNumber(int historyNumber) { this.historyNumber = historyNumber; }
    public void setNegotiations(List<Negotiation> negotiations) { this.negotiations = negotiations; }
    public void setReserved(int reserved) { this.reserved = reserved; }
    public void setNegotiation(Map<String, Boolean> negotiation) {
        this.negotiation = negotiation;
    }
    public void setReservedUsers(Map<String, Boolean> reservedUsers) { this.reservedUsers = reservedUsers; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public void addNegotiations(Negotiation negotiation) { this.negotiations.add(negotiation); }
    public void addReserved(int reserved) { this.reserved+=reserved; }



}

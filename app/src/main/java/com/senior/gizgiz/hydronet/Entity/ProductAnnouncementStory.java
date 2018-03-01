package com.senior.gizgiz.hydronet.Entity;

import com.senior.gizgiz.hydronet.Adapter.ListViewAdapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProductAnnouncementStory extends Story {
    private String saleStatus;
    private List<Negotiation> negotiations;

    public ProductAnnouncementStory(User owner, String detail, Plant mentionedPlant) {
        super(owner, StoryAdapter.getTypeSale(),detail,mentionedPlant);
        this.saleStatus = "open";
        this.negotiations = new ArrayList<>();
    }

    public String getSaleStatus() { return saleStatus; }
    public List<Negotiation> getNegotiations() { return negotiations; }

    public void setSaleStatus(String saleStatus) { this.saleStatus = saleStatus; }
    public void addNegotiations(Negotiation negotiation) { this.negotiations.add(negotiation); }
}

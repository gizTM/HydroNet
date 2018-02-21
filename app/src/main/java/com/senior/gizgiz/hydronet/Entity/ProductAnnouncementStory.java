package com.senior.gizgiz.hydronet.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 2018/02/06.
 */

public class ProductAnnouncementStory extends Story {
    private String status;
    private List<Negotiation> negotiations;

    public ProductAnnouncementStory(User owner, String detail, Plant mentionedPlant) {
        super(owner,"sale",detail,mentionedPlant);
        this.status = "open";
        this.negotiations = new ArrayList<>();
    }

    public String getStatus() { return status; }
    public List<Negotiation> getNegotiations() { return negotiations; }

    public void setStatus(String status) { this.status = status; }
    public void addNegotiations(Negotiation negotiation) { this.negotiations.add(negotiation); }
}

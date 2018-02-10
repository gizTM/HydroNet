package com.senior.gizgiz.hydronet.Entity;

/**
 * Created by Admins on 2018/02/06.
 */

public class Negotiation {
    private User negotiator;
    private String condition, status;

    public Negotiation(User negotiator, String condition) {
        this.negotiator = negotiator;
        this.condition = condition;
        this.status = "open";
    }
}

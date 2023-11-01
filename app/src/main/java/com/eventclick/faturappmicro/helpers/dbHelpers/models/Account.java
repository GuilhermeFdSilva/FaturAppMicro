package com.eventclick.faturappmicro.helpers.dbHelpers.models;

import java.sql.Date;

public class Account {
    private Long id;
    private Long clientId;
    private double value;
    private Date created;
    private Date expiration;
    private boolean paid;

    public Account(Long id, Long clientId, double value, Date created, Date expiration, boolean paid) {
        this.id = id;
        this.clientId = clientId;
        this.value = value;
        this.created = created;
        this.expiration = expiration;
        this.paid = paid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

package com.eventclick.faturappmicro.helpers.dbHelpers.models;

import java.sql.Date;

public class Account {
    private Long id;
    private Long clientId;
    private String description;
    private double value;
    private Date created;
    private Date expiration;
    private boolean paid;

    public Account(Long id, Long clientId, String description, double value, Date created, Date expiration, boolean paid) {
        this.id = id;
        this.clientId = clientId;
        this.description = description;
        this.value = value;
        this.created = created;
        this.expiration = expiration;
        this.paid = paid;
    }

    public Account(Long clientId, String description, double value, Date expiration, boolean paid) {
        this.clientId = clientId;
        this.description = description;
        this.value = value;
        this.created = new Date(new java.util.Date().getTime());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

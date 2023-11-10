package com.eventclick.faturappmicro.helpers.dbHelpers.models;

import java.sql.Date;

/**
 * Modelo de dados para representação de uma conta
 */
public class Account {
    private Long id;
    private final Long clientId;
    private final String description;
    private final double value;
    private Date paidAt;
    private final Date expiration;
    private boolean paid;

    /**
     * Construtor da classe com todos os atributos
     *
     * @param id Identificador da conta
     * @param clientId Identificador do cliente a quem a conta pertence
     * @param description Descrição da conta
     * @param value Valor da conta
     * @param paidAt Data do pagamento da conta
     * @param expiration Data de vencimento da conta
     * @param paid Indica se a conta foi paga
     */
    public Account(Long id, Long clientId, String description, double value, Date paidAt, Date expiration, boolean paid) {
        this.id = id;
        this.clientId = clientId;
        this.description = description;
        this.value = value;
        this.paidAt = paidAt;
        this.expiration = expiration;
        this.paid = paid;
    }

    /**
     * Construtor da classe sem identificador
     *
     * @param clientId Identificador do cliente a quem a conta pertence
     * @param description Descrição da conta
     * @param value Valor da conta
     * @param paidAt Data do pagamento da conta
     * @param expiration Data de vencimento da conta
     * @param paid Indica se a conta foi paga
     */
    public Account(Long clientId, String description, double value, Date paidAt, Date expiration, boolean paid) {
        this.clientId = clientId;
        this.description = description;
        this.value = value;
        this.paidAt = paidAt;
        this.expiration = expiration;
        this.paid = paid;
    }

    /**
     * Construtor da classe sem o identificador e data do pagamento
     *
     * @param clientId Identificador do cliente a quem a conta pertence
     * @param description Descrição da conta
     * @param value Valor da conta
     * @param expiration Data de vencimento da conta
     * @param paid Indica se a conta foi paga
     */
    public Account(Long clientId, String description, double value, Date expiration, boolean paid) {
        this.clientId = clientId;
        this.description = description;
        this.value = value;
        this.paidAt = null;
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

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}

package com.eventclick.faturappmicro.helpers.dbHelpers.models;

public class Client {
    private Long id;
    private String name;
    private String cpf_cnpj;
    private String contact;
    private String address;
    private String pix;

    public Client(Long id, String name, String cpf_cnpj, String contact, String address, String pix) {
        this.id = id;
        this.name = name;
        this.cpf_cnpj = cpf_cnpj;
        this.contact = contact;
        this.address = address;
        this.pix = pix;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPix() {
        return pix;
    }

    public void setPix(String pix) {
        this.pix = pix;
    }
}

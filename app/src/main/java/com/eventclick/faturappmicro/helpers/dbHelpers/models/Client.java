package com.eventclick.faturappmicro.helpers.dbHelpers.models;

/**
 * Modelo de dados para representação de um cliente
 */
public class Client {
    private Long id;
    private String name;
    private String cpf_cnpj;
    private String contact;
    private String address;
    private String pix;

    /**
     * Construtor da classe com todos os atributos
     *
     * @param id Identificador do cliente
     * @param name Nome do cliente
     * @param cpf_cnpj CPF ou CNPJ do cliente
     * @param contact Contato do cliente
     * @param address Endereço do cliente
     * @param pix Chave PIX do cliente
     */
    public Client(Long id, String name, String cpf_cnpj, String contact, String address, String pix) {
        this.id = id;
        this.name = name;
        this.cpf_cnpj = cpf_cnpj;
        this.contact = contact;
        this.address = address;
        this.pix = pix;
    }

    /**
     * Contrutor da classe sem o identificador do cliente
     *
     * @param name Nome do cliente
     * @param cpf_cnpj CPF ou CNPJ do cliente
     * @param contact Contato do cliente
     * @param address Endereço do cliente
     * @param pix Chave PIX do cliente
     */
    public Client(String name, String cpf_cnpj, String contact, String address, String pix) {
        this.name = name;
        this.cpf_cnpj = cpf_cnpj;
        this.contact = contact;
        this.address = address;
        this.pix = pix;
    }

    /**
     * Contrutor da classe para uma instância somente com id e nome do cliente
     *
     * @param id Identificador do cliente
     * @param name Nome do cliente
     */
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

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getPix() {
        return pix;
    }
}

package com.eventclick.faturappmicro.helpers.dbHelpers.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eventclick.faturappmicro.helpers.dbHelpers.DbHelper;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações relacionadas a clientes no banco de dados
 */
public class ClientDAO implements IDAO<Client>{
    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    /**
     * Construtor da classe
     *
     * @param context Contexto da aplicação
     */
    public ClientDAO (Context context) {
        DbHelper helper = new DbHelper(context);
        write = helper.getWritableDatabase();
        read = helper.getReadableDatabase();
    }

    /**
     * Salvar novo cliente
     *
     * @param client Novo cliente a ser registrado
     * @return TRUE se a operação for bem sucedida, FALSE caso não
     */
    @Override
    public boolean save(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", client.getName());
        contentValues.put("cpf_cnpj", client.getCpf_cnpj());
        contentValues.put("contact", client.getContact());
        contentValues.put("address", client.getAddress());
        contentValues.put("pix", client.getPix());

        try {
            // Insere os dados na tabela 'clientes'
            write.insert(DbHelper.CLIENT_TABLE, null, contentValues);
        } catch (Exception e) {
            Log.i("INFO DB", "error at save client" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Atualiza cliente existente
     *
     * @param client Cliente com os dados atualizados
     * @return TRUE se a operação for bem sucedida, FALSE caso não
     */
    @Override
    public boolean update(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", client.getName());
        contentValues.put("cpf_cnpj", client.getCpf_cnpj());
        contentValues.put("contact", client.getContact());
        contentValues.put("address", client.getAddress());
        contentValues.put("pix", client.getPix());

        try {
            // Atualiza os dados do cliente na tabela 'clientes' usando ID como filtro
            String[] args = {client.getId().toString()};
            write.update(DbHelper.CLIENT_TABLE, contentValues, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at update client" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Exclui um cliente
     *
     * @param client Cliente a ser deletado
     * @return TRUE se a operação for bem sucedida, FALSE caso não
     */
    @Override
    public boolean delete(Client client) {
        String[] args = {client.getId().toString()};

        try{
            // Deleta um cliente utilizando ID como filtro
            write.delete(DbHelper.CLIENT_TABLE, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at delete client" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Encontra um cliente com base no ID
     *
     * @param idItem ID do cliete a ser localizado
     * @return Retorna um cliente com os dados encontrados
     */
    @Override
    public Client getById(int idItem) {
        String sql = String.format("SELECT * FROM %s WHERE id=%d", DbHelper.CLIENT_TABLE, idItem);

        Cursor cursor = read.rawQuery(sql, null);

        if (cursor.isNull(0)) {
            return null;
        }

        int indexId = cursor.getColumnIndex("id");
        int indexName = cursor.getColumnIndex("name");
        int indexCpf_cnpj = cursor.getColumnIndex("cpf_cnpj");
        int indexContact = cursor.getColumnIndex("contact");
        int indexAddress = cursor.getColumnIndex("address");
        int indexPix = cursor.getColumnIndex("pix");

        Long id = cursor.getLong(indexId);
        String name = cursor.getString(indexName);
        String cpf_cnpj = cursor.getString(indexCpf_cnpj);
        String contact  = cursor.getString(indexContact);
        String address = cursor.getString(indexAddress);
        String pix = cursor.getString(indexPix);

        return new Client(id, name, cpf_cnpj, contact, address, pix);
    }

    /**
     * Lista todos os clientes cadastrados
     *
     * @return Uma lista com todos os clientes
     */
    @Override
    public List<Client> list() {
        List<Client> clients = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s", DbHelper.CLIENT_TABLE);
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexCpf_cnpj = cursor.getColumnIndex("cpf_cnpj");
            int indexContact = cursor.getColumnIndex("contact");
            int indexAddress = cursor.getColumnIndex("address");
            int indexPix = cursor.getColumnIndex("pix");

            Long id = cursor.getLong(indexId);
            String name = cursor.getString(indexName);
            String cpf_cnpj = cursor.getString(indexCpf_cnpj);
            String contact  = cursor.getString(indexContact);
            String address = cursor.getString(indexAddress);
            String pix = cursor.getString(indexPix);

            Client client = new Client(id, name, cpf_cnpj, contact, address, pix);
            clients.add(client);
        }
        cursor.close();
        return clients;
    }
}

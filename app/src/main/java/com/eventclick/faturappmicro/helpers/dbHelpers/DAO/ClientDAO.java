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

public class ClientDAO implements IDAO<Client>{
    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ClientDAO (Context context) {
        DbHelper helper = new DbHelper(context);
        write = helper.getWritableDatabase();
        read = helper.getReadableDatabase();
    }

    @Override
    public boolean save(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", client.getName());
        contentValues.put("cpf_cnpj", client.getCpf_cnpj());
        contentValues.put("contact", client.getContact());
        contentValues.put("address", client.getAddress());
        contentValues.put("pix", client.getPix());

        try {
            write.insert(DbHelper.CLIENT_TABLE, null, contentValues);
        } catch (Exception e) {
            Log.i("INFO DB", "error at save client" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean update(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", client.getName());
        contentValues.put("cpf_cnpj", client.getCpf_cnpj());
        contentValues.put("contact", client.getContact());
        contentValues.put("address", client.getAddress());
        contentValues.put("pix", client.getPix());

        try {
            String[] args = {client.getId().toString()};
            write.update(DbHelper.CLIENT_TABLE, contentValues, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at update client" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Client client) {
        String[] args = {client.getId().toString()};

        try{
            write.delete(DbHelper.CLIENT_TABLE, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at delete client" + e.getMessage());
            return false;
        }

        return true;
    }

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

package com.eventclick.faturappmicro.helpers.dbHelpers.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eventclick.faturappmicro.helpers.dbHelpers.DbHelper;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IDAO<Account>{
    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public AccountDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        write = helper.getWritableDatabase();
        read = helper.getReadableDatabase();
    }

    @Override
    public boolean save(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("client_id", account.getClientId());
        contentValues.put("description", account.getDescription());
        contentValues.put("value", account.getValue());
        contentValues.put("paid_at", account.getPaidAt() == null ? 0 : account.getPaidAt().getTime());
        contentValues.put("expiration", account.getExpiration().getTime());
        contentValues.put("paid", account.isPaid());

        try {
            write.insert(DbHelper.ACCOUNTS_TABLE, null, contentValues);
        } catch (Exception e) {
            Log.i("INFO DB", "error at save account" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean update(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("client_id", account.getClientId());
        contentValues.put("description", account.getDescription());
        contentValues.put("value", account.getValue());
        contentValues.put("paid_at", account.getPaidAt().getTime());
        contentValues.put("expiration", account.getExpiration().getTime());
        contentValues.put("paid", account.isPaid());

        try {
            String[] args = {account.getId().toString()};
            write.update(DbHelper.ACCOUNTS_TABLE, contentValues, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at update account" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Account account) {
        String[] args = {account.getId().toString()};

        try {
            write.delete(DbHelper.ACCOUNTS_TABLE, "id=?", args);
        } catch (Exception e) {
            Log.i("INFO DB", "error at delete account" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Account getById(int idItem) {
        String sql = String.format("SELECT * FROM %s WHERE id=%d", DbHelper.ACCOUNTS_TABLE, idItem);

        Cursor cursor = read.rawQuery(sql, null);

        if (cursor.isNull(0)) {
            return null;
        }

        int indexId = cursor.getColumnIndex("id");
        int indexClientId = cursor.getColumnIndex("client_id");
        int indexDescription = cursor.getColumnIndex("description");
        int indexValue = cursor.getColumnIndex("value");
        int indexPaidAt = cursor.getColumnIndex("paid_at");
        int indexExpiration = cursor.getColumnIndex("expiration");
        int indexPaid = cursor.getColumnIndex("paid");

        Long id = cursor.getLong(indexId);
        Long clientId = cursor.getLong(indexClientId);
        String description = cursor.getString(indexDescription);
        double value = cursor.getDouble(indexValue);
        Long paidAt = cursor.getLong(indexPaidAt);
        Long expiration = cursor.getLong(indexExpiration);
        boolean paid = cursor.getInt(indexPaid) == 1;

        Account account = new Account(id, clientId, description, value, new Date(paidAt), new Date(expiration), paid);

        return account;
    }

    @Override
    public List<Account> list() {
        List<Account> accounts = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s", DbHelper.ACCOUNTS_TABLE);
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex("id");
            int indexClientId = cursor.getColumnIndex("client_id");
            int indexDescription = cursor.getColumnIndex("description");
            int indexValue = cursor.getColumnIndex("value");
            int indexPaidAt = cursor.getColumnIndex("paid_at");
            int indexExpiration = cursor.getColumnIndex("expiration");
            int indexPaid = cursor.getColumnIndex("paid");

            Long id = cursor.getLong(indexId);
            Long clientId = cursor.getLong(indexClientId);
            String description = cursor.getString(indexDescription);
            double value = cursor.getDouble(indexValue);
            Long paidAt = cursor.getLong(indexPaidAt);
            Long expiration = cursor.getLong(indexExpiration);
            boolean paid = cursor.getInt(indexPaid) == 1;

            Account account = new Account(id, clientId, description, value, new Date(paidAt), new Date(expiration), paid);
            accounts.add(account);
        }

        return accounts;
    }

    public List<Account> getByClientId(Long clientId) {
        List<Account> accounts = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s WHERE client_id=%d", DbHelper.ACCOUNTS_TABLE, clientId);
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex("id");
            int indexDescription = cursor.getColumnIndex("description");
            int indexValue = cursor.getColumnIndex("value");
            int indexPaidAt = cursor.getColumnIndex("paid_at");
            int indexExpiration = cursor.getColumnIndex("expiration");
            int indexPaid = cursor.getColumnIndex("paid");

            Long id = cursor.getLong(indexId);
            String description = cursor.getString(indexDescription);
            double value = cursor.getDouble(indexValue);
            Long paidAt = cursor.getLong(indexPaidAt);
            Long expiration = cursor.getLong(indexExpiration);
            boolean paid = cursor.getInt(indexPaid) == 1;

            Account account = new Account(id, clientId, description, value, new Date(paidAt), new Date(expiration), paid);
            accounts.add(account);
        }

        return accounts;
    }
}

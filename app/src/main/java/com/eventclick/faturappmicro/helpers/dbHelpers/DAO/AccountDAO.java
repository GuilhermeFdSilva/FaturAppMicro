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
        contentValues.put("value", account.getValue());
        contentValues.put("created", account.getCreated().getTime());
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
        contentValues.put("value", account.getValue());
        contentValues.put("created", account.getCreated().getTime());
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
    public List<Account> list() {
        List<Account> accounts = new ArrayList<>();

        String sql = String.format("SELECT * FROM %s", DbHelper.ACCOUNTS_TABLE);
        Cursor cursor = read.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int indexId = cursor.getColumnIndex("id");
            int indexClientId = cursor.getColumnIndex("client_id");
            int indexValue = cursor.getColumnIndex("value");
            int indexCreated = cursor.getColumnIndex("created");
            int indexExpiration = cursor.getColumnIndex("expiration");
            int indexPaid = cursor.getColumnIndex("paid");

            Long id = cursor.getLong(indexId);
            Long clientId = cursor.getLong(indexClientId);
            double value = cursor.getDouble(indexValue);
            Long created = cursor.getLong(indexCreated);
            Long expiration = cursor.getLong(indexExpiration);
            boolean paid = cursor.getInt(indexPaid) == 1;

            Account account = new Account(id, clientId, value, new Date(created), new Date(expiration), paid);
            accounts.add(account);
        }

        return accounts;
    }
}

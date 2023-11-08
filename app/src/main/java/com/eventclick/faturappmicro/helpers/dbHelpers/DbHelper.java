package com.eventclick.faturappmicro.helpers.dbHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    public static int VERSION = 1;
    public static final String NAME_DB = "DB_FINANCE";
    public static final String CLIENT_TABLE = "clients";
    public static final String ACCOUNTS_TABLE = "accounts";

    public DbHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlClient = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR(35) NOT NULL," +
                        "cpf_cnpj VARCHAR (18)," +
                        "contact VARCHAR(14)," +
                        "address VARCHAR (120)," +
                        "pix VARCHAR(180)" +
                ");", CLIENT_TABLE);

        String sqlAccount = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "client_id INTEGER," +
                        "description VARCHAR(20) NOT NULL," +
                        "value DECIMAL(8, 2) NOT NULL," +
                        "paid_at DATE," +
                        "expiration DATE," +
                        "paid BOOLEAN NOT NULL," +
                        "FOREIGN KEY (client_id) REFERENCES %s(id)" +
                ");", ACCOUNTS_TABLE, CLIENT_TABLE);
        try {
            sqLiteDatabase.execSQL(sqlClient);
            sqLiteDatabase.execSQL(sqlAccount);
            Log.i("INFO DB", "successful database creation");
        } catch (Exception e) {
            Log.i("INFO DB", "error at create database" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

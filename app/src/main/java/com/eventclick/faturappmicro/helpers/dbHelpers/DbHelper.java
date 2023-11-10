package com.eventclick.faturappmicro.helpers.dbHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Classe para inicializar e gerenciar o banco de dados SQLite
 * Estende SQLiteOpenHelper
 */
public class DbHelper extends SQLiteOpenHelper {
    public static int VERSION = 1;
    public static final String NAME_DB = "DB_FINANCE";
    public static final String CLIENT_TABLE = "clients";
    public static final String ACCOUNTS_TABLE = "accounts";

    /**
     * Construtor da classe
     *
     * @param context Contexto da aplicação
     */
    public DbHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    /**
     * Chamado quando o banco de dados é utilizado pela primeira vez
     *
     * @param sqLiteDatabase Instância do banco de dados
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Estrutura da tabela 'clients'
        String sqlClient = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR(35) NOT NULL," +
                        "cpf_cnpj VARCHAR (18)," +
                        "contact VARCHAR(14)," +
                        "address VARCHAR (120)," +
                        "pix VARCHAR(180)" +
                ");", CLIENT_TABLE);

        // Estrutura da tabela 'accounts'
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
            // Executa os comendos SQL para criação das tabelas
            sqLiteDatabase.execSQL(sqlClient);
            sqLiteDatabase.execSQL(sqlAccount);
            Log.i("INFO DB", "successful database creation");
        } catch (Exception e) {
            Log.i("INFO DB", "error at create database" + e.getMessage());
        }
    }

    /**
     * Método utilizado para atualizar o banco de dados
     * Não se aplica nesse contexto
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}

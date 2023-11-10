package com.eventclick.faturappmicro.helpers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private final SharedPreferences PREFERENCES;
    private final SharedPreferences.Editor EDITOR;
    public final static String KEY_NAME = "name";
    public final static String KEY_CNPJ = "cnpj";

    /**
     * Construtor para inicializar o acessar às preferências do usuário
     *
     * @param context Utilizado para acessar as preferências
     */
    public UserPreferences (Context context) {
        this.PREFERENCES =context.getSharedPreferences("company.preferences", 0);
        this.EDITOR = PREFERENCES.edit();
    }

    /**
     * Atualiza as preferências do usuário
     *
     * @param key Chave de acesso para o dado a ser atualizado
     * @param content Conteúdo a ser inserido como preferência
     */
    public void save (String key, String content) {
        EDITOR.putString(key, content);
        EDITOR.apply();
    }

    /**
     * Obtém o valor da preferência associado à chave
     *
     * @param key Chave da preferência desejada
     * @return O valor da preferência, ou uma String vazia se a preferência não existir
     */
    public String getPreference (String key) {
        return PREFERENCES.getString(key, "");
    }
}

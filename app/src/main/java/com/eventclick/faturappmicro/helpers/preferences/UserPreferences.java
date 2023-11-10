package com.eventclick.faturappmicro.helpers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    public final static String KEY_NAME = "name";
    public final static String KEY_CNPJ = "cnpj";

    /**
     * Construtor para inicializar o acessar às preferências do usuário
     *
     * @param context Utilizado para acessar as preferências
     */
    public UserPreferences (Context context) {
        this.preferences =context.getSharedPreferences("company.preferences", 0);
        this.editor = preferences.edit();
    }

    /**
     * Atualiza as preferências do usuário
     *
     * @param key Chave de acesso para o dado a ser atualizado
     * @param content Conteúdo a ser inserido como preferência
     */
    public void save (String key, String content) {
        editor.putString(key, content);
        editor.apply();
    }

    /**
     * Obtém o valor da preferência associado à chave
     *
     * @param key Chave da preferência desejada
     * @return O valor da preferência, ou uma String vazia se a preferência não existir
     */
    public String getPreference (String key) {
        return preferences.getString(key, "");
    }
}

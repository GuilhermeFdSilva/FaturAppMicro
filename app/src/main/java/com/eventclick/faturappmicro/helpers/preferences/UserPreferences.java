package com.eventclick.faturappmicro.helpers.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "company.preferences";
    public final String KEY_NAME = "name";
    public final String KEY_CNPJ = "cnpj";

    public UserPreferences (Context context) {
        this.context = context;
        this.preferences =context.getSharedPreferences(FILE_NAME, 0);
        this.editor = preferences.edit();
    }

    public void save (String key, String content) {
        editor.putString(key, content);
        editor.commit();
    }

    public String getPreference (String key) {
        return preferences.getString(key, "");
    }
}

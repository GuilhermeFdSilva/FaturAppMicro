package com.eventclick.faturappmicro.helpers.filters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class filterDate implements TextWatcher {
    private EditText editText;
    private boolean isUpdating = false;

    public filterDate(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        String unmask = editable.toString().replaceAll("[^0-9]*", "");
        String mask = "##/##/####";
        String formattedString = "";

        int index = 0;
        for (int i = 0; i < mask.length(); i++) {
            if (index == unmask.length()) {
                break;
            }

            char c = mask.charAt(i);
            if (c == '#') {
                formattedString += unmask.charAt(index);
                index++;
            } else {
                formattedString += c;
            }
        }

        isUpdating = true;
        editText.setText(formattedString);
        editText.setSelection(formattedString.length());
    }
}

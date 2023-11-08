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

            char cMask = mask.charAt(i);
            char cUnmask = unmask.charAt(index);

            if (cMask == '#') {
                if (index == 0 && Integer.parseInt(String.valueOf(cUnmask)) > 3){
                    formattedString += "0";
                    unmask = "0" + cUnmask;
                    i++;
                    index++;
                } else if (index == 1 && unmask.charAt(0) == '3' && Integer.parseInt(String.valueOf(cUnmask)) > 1) {
                    break;
                } else if (index == 2 && Integer.parseInt(String.valueOf(cUnmask)) > 1) {
                    formattedString += "0";
                    unmask = unmask.substring(0, 2);
                    unmask += "0" + cUnmask;
                    i++;
                    index++;
                } else if (index == 3 && unmask.charAt(2) == '1' && Integer.parseInt(String.valueOf(cUnmask)) > 2) {
                    break;
                } else if (index == 4 && Integer.parseInt(String.valueOf(cUnmask)) > 2){
                    formattedString += "202";
                    unmask = unmask.substring(0, 4);
                    unmask += "202" + cUnmask;
                    i += 3;
                    index += 3;
                } else if (index == 5 && unmask.charAt(4) == '1' && Integer.parseInt(String.valueOf(cUnmask)) < 9) {
                    break;
                } else if (index == 5 && unmask.charAt(4) == '2' && Integer.parseInt(String.valueOf(cUnmask)) > 0) {
                    break;
                }
                formattedString += cUnmask;
                index++;
            } else {
                formattedString += cMask;
            }
        }

        isUpdating = true;
        editText.setText(formattedString);
        editText.setSelection(formattedString.length());
    }
}

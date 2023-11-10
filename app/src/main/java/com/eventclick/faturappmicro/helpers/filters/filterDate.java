package com.eventclick.faturappmicro.helpers.filters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Classe para filtrar e formatar automaticamente datas em um EditText
 * Implementa TextWatcher para monitorar alterações no texto
 */
public class filterDate implements TextWatcher {
    private final EditText EDIT_TEXT;
    private boolean isUpdating = false;

    /**
     * Contrutor da classe
     *
     * @param editText O EditText que será monitorado
     */
    public filterDate(EditText editText) {
        this.EDIT_TEXT = editText;
    }

    /**
     * Método chamado antes da alteração no texto
     * Não se aplica nesse contexto
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * Método chamado quando há alteração no texto
     * Não se aplica nesse contexto
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * Método chamado depois que o texto é alterado
     *
     * @param editable O texto após as alterações
     */
    @Override
    public void afterTextChanged(Editable editable) {
        // Evita loop infinito
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        // String atual, excluindo todos os caracteres especiais aplicados
        String unmask = editable.toString().replaceAll("[^0-9]*", "");
        //Mascara desejada
        String mask = "##/##/####";
        /*
            String atualizada com os characteres digitados
            Mais os caracteres especiais que se aplicam da mascara
         */
        StringBuilder formattedString = new StringBuilder();

        // Indice para percorrer a String sem caracteres especiais
        int index = 0;

        // Loop para as Strings limitado ao tamanho da mascara
        for (int i = 0; i < mask.length(); i++) {
            /*
                Evita OutOfBounds.Exeption
                Parando o loop caso o indice seja maior que a String sem caracteres especiais
             */
            if (index == unmask.length()) {
                break;
            }

            // Caracteres atuais a serem verificados
            char cMask = mask.charAt(i);
            char cUnmask = unmask.charAt(index);

            /*
                Caso o caractere atual da mascara seja #
                Utiliza na String atualizada o valor do caractere da String sem caracteres especiais
                Caso contrario adiciona o valor do caractere da mascara = /
             */
            if (cMask == '#') {
                // Evita que o usuário coloque um valor para dia maior que 31
                if (index == 0 && cUnmask > '3'){
                    formattedString.append("0");
                    unmask = "0" + cUnmask;
                    i++;
                    index++;
                } else if (index == 1 && unmask.charAt(0) == '3' && cUnmask > '1') {
                    break;
                } else if (index == 2 && cUnmask > '1') { // Evita que o usuário coloque um valor para mês maior que 12
                    formattedString.append("0");
                    unmask = unmask.substring(0, 2);
                    unmask += "0" + cUnmask;
                    i++;
                    index++;
                } else if (index == 3 && unmask.charAt(2) == '1' && cUnmask > '2') {
                    break;
                } else if (index == 4 && cUnmask > '2'){ // Evita que o usúario coloque valores para ano maiores que 2099 ou menores que 1900
                    formattedString.append("202");
                    unmask = unmask.substring(0, 4);
                    unmask += "202" + cUnmask;
                    i += 3;
                    index += 3;
                } else if (index == 5 && unmask.charAt(4) == '1' && cUnmask < '9') {
                    break;
                } else if (index == 5 && unmask.charAt(4) == '2' && cUnmask > '0') {
                    break;
                }
                formattedString.append(cUnmask);
                index++;
            } else {
                formattedString.append(cMask);
            }
        }

        isUpdating = true;
        EDIT_TEXT.setText(formattedString.toString());
        EDIT_TEXT.setSelection(formattedString.length());
    }
}

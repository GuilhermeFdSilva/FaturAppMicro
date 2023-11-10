package com.eventclick.faturappmicro.helpers.filters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Classe para filtrar e formatar automaticamente CPF ou CNPJ em um EditText
 * Implementa TextWatcher para monitorar alterações no texto
 */
public class filterCpfCnpj implements TextWatcher {
    private final EditText editText;
    private boolean isUpdating = false;

    /**
     * Construtor da classe
     *
     * @param editText O EditText que será monitorado
     */
    public filterCpfCnpj(EditText editText) {
        this.editText = editText;
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
        String mask = "###.###.###-##";
        /*
            String atualizada com os characteres digitados
            Mais os caracteres especiais que se aplicam da mascara
         */
        StringBuilder formattedString = new StringBuilder();

        // Indice para percorrer a String sem caracteres especiais
        int index = 0;

        // Loop para as Strings limitado ao tamanho da mascara
        for (int i = 0; i < mask.length(); i++) {
            // Muda a mascara para o formato de CNPJ caso a string seja maior que a mascara anterior
            if (unmask.length() > 11) {
                mask = "##.###.###/####-##";
            }
            /*
                Evita OutOfBounds.Exeption
                Parando o loop caso o indice seja maior que a String sem caracteres especiais
             */
            if (index == unmask.length()) {
                break;
            }

            // Caractere atual a ser verificado
            char c = mask.charAt(i);
            /*
                Caso o caractere atual da mascara seja #
                Utiliza na String atualizada o valor do caractere da String sem caracteres especiais
                Caso contrario adiciona o valor do caractere da mascara (. / -)
             */
            if (c == '#') {
                formattedString.append(unmask.charAt(index));
                index++;
            } else {
                formattedString.append(c);
            }
        }

        isUpdating = true;
        editText.setText(formattedString.toString());
        editText.setSelection(formattedString.length());
    }
}

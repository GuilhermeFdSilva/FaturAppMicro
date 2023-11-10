package com.eventclick.faturappmicro.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;

/**
 * Adaptador personalizado para preencher o Spinner de clientes
 */
public class AdapterSpinner extends BaseAdapter {
    private final Context context;
    private final List<Client> clients;

    /**
     * Construtor da classe
     *
     * @param context Contexto da aplicação
     * @param clients Lista de clientes para o Spinner
     */
    public AdapterSpinner(Context context, List<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    /**
     * Obtém o número de itens
     *
     * @return Número de clientes
     */
    @Override
    public int getCount() {
        return clients.size();
    }

    /**
     * Obtém um item específico
     *
     * @param i Posição do item
     * @return Cliente na posição i
     */
    @Override
    public Object getItem(int i) {
        return clients.get(i);
    }

    /**
     * Obtém o ID do item
     *
     * @param i Posição do item
     * @return ID do cliente na posição i
     */
    @Override
    public long getItemId(int i) {
        return clients.get(i).getId();
    }

    /**
     * Obtém a View que será exibida no Spinner
     *
     * @param i Posição do item na lista
     * @param view A View utilizada
     * @param viewGroup O ViewGroup pai
     * @return View formatada para exibição
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_spinner_list, null);

        TextView textName = view.findViewById(R.id.textName);
        textName.setText(clients.get(i).getName());

        return view;
    }
}

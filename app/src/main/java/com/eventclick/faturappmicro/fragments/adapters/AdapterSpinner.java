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

public class AdapterSpinner extends BaseAdapter {
    private Context context;
    private List<Client> clients;

    public AdapterSpinner(Context context, List<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Object getItem(int i) {
        return clients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return clients.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_spinner_list, null);

        TextView textName = view.findViewById(R.id.textName);
        textName.setText(clients.get(i).getName());

        return view;
    }
}

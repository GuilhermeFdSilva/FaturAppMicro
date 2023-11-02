package com.eventclick.faturappmicro.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;

public class AdapterClients extends RecyclerView.Adapter<AdapterClients.MyViewHolder> {

    private List<Client> clients;

    public AdapterClients(List<Client> clients) {
        this.clients = clients;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_clients_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textClientName, textClientInfo;
        private ImageView imageDeleteClient;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textClientName = itemView.findViewById(R.id.textClientsName);
            textClientInfo = itemView.findViewById(R.id.textClientsInfo);
            imageDeleteClient = itemView.findViewById(R.id.imageDeletCliente);
        }
    }
}

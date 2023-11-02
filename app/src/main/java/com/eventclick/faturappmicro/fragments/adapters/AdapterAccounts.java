package com.eventclick.faturappmicro.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.MyViewHolder> {

    private List<Account> accounts;
    private List<Client> clients;

    public AdapterAccounts(List<Account> accounts, List<Client> clients) {
        this.accounts = accounts;
        this.clients = clients;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_accounts_list, parent, false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textAccountDesc, textClientAccount, textAccountValue;
        private ImageView imagePaid, imageDeleteAccount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textAccountDesc = itemView.findViewById(R.id.textAccountsDesc);
            this.textClientAccount = itemView.findViewById(R.id.textClientAccounts);
            this.textAccountValue = itemView.findViewById(R.id.textAccountsVlaue);
            this.imagePaid = itemView.findViewById(R.id.imagePaid);
            this.imageDeleteAccount = itemView.findViewById(R.id.imageDeletAccount);
        }
    }
}

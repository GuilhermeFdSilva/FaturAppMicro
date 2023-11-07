package com.eventclick.faturappmicro.fragments.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.AccountsFragment;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;
import java.util.Optional;

public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.MyViewHolder> {

    private Context context;
    private AccountsFragment fragment;

    private List<Account> accounts;
    private List<Client> clients;

    public AdapterAccounts(Context context, AccountsFragment fragment, List<Account> accounts, List<Client> clients) {
        this.context = context;
        this.fragment = fragment;
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
        Account account = accounts.get(position);

        holder.textAccountDesc.setText(
                account.getDescription().length() > 13 ?
                        String.format("%s...", account.getDescription().substring(0, 13)):
                        account.getDescription()
        );

        Optional<Client> clientOptional = clients.stream()
                .filter(client -> client.getId() == account.getClientId())
                .findAny();
        String clientName = clientOptional.isPresent() ? clientOptional.get().getName() : "Não identificado";
        holder.textClientAccount.setText(clientName);

        double value = account.getValue();
        String sValue = String.format("R$ %.2f", value).replace(".", ",");
        holder.textAccountValue.setText(sValue);

        if (value < 0) {
            holder.textAccountValue.setTextColor(ContextCompat.getColor(holder.textAccountValue.getContext(), R.color.red));
        }

        if (!account.isPaid()) {
            holder.imagePaid.setImageResource(R.drawable.ic_not_paid);
        } else {
            holder.imagePaid.setImageResource(R.drawable.ic_paid);
        }

        holder.imagePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account.setPaid(!account.isPaid());
                if (new AccountDAO(context).update(account)) {
                    fragment.getRegisters();
                }
            }
        });

        holder.imageDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                int greenColor = ContextCompat.getColor(view.getContext(), R.color.primaryDark);
                int redColor = ContextCompat.getColor(view.getContext(), R.color.red);

                SpannableString positiveButton = new SpannableString("Deletar");
                positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString negativeButton = new SpannableString("Cancelar");
                negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.setTitle("Deletar")
                        .setMessage("Deletar: " + account.getDescription())
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (new AccountDAO(view.getContext()).delete(account)){
                                    fragment.getRegisters();
                                } else {
                                    Toast.makeText(view.getContext(), "Erro ao deletar conta", Toast.LENGTH_LONG);
                                }
                            }
                        })
                        .setNegativeButton(negativeButton, null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
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

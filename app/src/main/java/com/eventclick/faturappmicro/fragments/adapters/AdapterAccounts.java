package com.eventclick.faturappmicro.fragments.adapters;

import android.content.Context;
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

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.AccountsFragment;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador personalizado para preencher o RecyclerView com dados das contas
 */
public class AdapterAccounts extends RecyclerView.Adapter<AdapterAccounts.MyViewHolder> {

    private final Context context;
    private final AccountsFragment fragment;

    private final List<Account> accounts;
    private final List<Client> clients;

    /**
     * Construtor da classe
     *
     * @param context Contexto da aplicação
     * @param fragment Fragment associado ao adaptador
     * @param accounts Lista de contas
     * @param clients Lista de clientes
     */
    public AdapterAccounts(Context context, AccountsFragment fragment, List<Account> accounts, List<Client> clients) {
        this.context = context;
        this.fragment = fragment;
        this.accounts = accounts;
        this.clients = clients;
    }

    /**
     * Cria novas instâncias de MyViewHolder associando ao layout
     *
     * @param parent   ViewGroup pai no qual a nova View será anexada
     * @param viewType Tipo de visualização do item
     * @return Instância de MyViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_accounts_list, parent, false);

        return new MyViewHolder(viewItem);
    }

    /**
     * Vincula os dados da conta à um item no RecyclerView
     *
     * @param holder   Instância de MyViewHolder
     * @param position Posição do item
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Account account = accounts.get(position);

        // Configura a descrição da conta, e limita os caracteres
        holder.textAccountDesc.setText(
                account.getDescription().length() > 13 ?
                        String.format("%s...", account.getDescription().substring(0, 13)) :
                        account.getDescription()
        );

        // Localiza o cliente dono da conta
        Optional<Client> clientOptional = clients.stream()
                .filter(client -> client.getId().equals(account.getClientId()))
                .findAny();
        String clientName = clientOptional.isPresent() ? clientOptional.get().getName() : "Não identificado";
        holder.textClientAccount.setText(clientName);

        // Configura o valor da conta formatado
        double value = account.getValue();
        String sValue = String.format("R$ %.2f", value).replace(".", ",");
        holder.textAccountValue.setText(sValue);

        // Caso o valor seja menor que 0 aplica a cor vermelha
        if (value < 0) {
            holder.textAccountValue.setTextColor(ContextCompat.getColor(holder.textAccountValue.getContext(), R.color.red));
        }

        // Caso a conta esteja paga, muda a cor do icone de pago
        if (!account.isPaid()) {
            holder.imagePaid.setImageResource(R.drawable.ic_not_paid);
        } else {
            holder.imagePaid.setImageResource(R.drawable.ic_paid);
        }

        // Configura OnClickListener de toggle de pago
        holder.imagePaid.setOnClickListener(view -> {
            account.setPaid(!account.isPaid());
            account.setPaidAt(new Date(new java.util.Date().getTime()));
            if (new AccountDAO(context).update(account)) {
                fragment.getRegisters();
                MainActivity.emitUpdate();
            } else {
                Toast.makeText(view.getContext(), "Erro ao pagar conta", Toast.LENGTH_LONG).show();
            }
        });

        // Configura OnClickListener exclusão de conta
        holder.imageDeleteAccount.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            // Obtendo as cores do R.color
            int greenColor = ContextCompat.getColor(view.getContext(), R.color.primaryDark);
            int redColor = ContextCompat.getColor(view.getContext(), R.color.red);

            // Configura uma String com cor definida vermelha
            SpannableString positiveButton = new SpannableString("Deletar");
            positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Configura uma String com cor definida vermelha
            SpannableString negativeButton = new SpannableString("Cancelar");
            negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Configuração do AlertDialog
            builder.setTitle("Deletar")
                    .setMessage("Deletar: " + account.getDescription())
                    .setPositiveButton(positiveButton, (dialogInterface, i) -> {
                        if (new AccountDAO(view.getContext()).delete(account)) {
                            fragment.getRegisters();
                            MainActivity.emitUpdate();
                        } else {
                            Toast.makeText(view.getContext(), "Erro ao deletar conta", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(negativeButton, null)
                    .create()
                    .show();
        });
    }

    /**
     * Obtém o número de itens da lista
     *
     * @return Número de itens da lista
     */
    @Override
    public int getItemCount() {
        return accounts.size();
    }

    /**
     * Classe interna de uma View de item
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textAccountDesc;
        private final TextView textClientAccount;
        private final TextView textAccountValue;
        private final ImageView imagePaid;
        private final ImageView imageDeleteAccount;

        /**
         * Construtorda classe interna
         *
         * @param itemView View de um item
         */
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

package com.eventclick.faturappmicro.fragments.adapters;

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
import com.eventclick.faturappmicro.fragments.ClientsFragment;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.ClientDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;

/**
 * Adaptador personalizado para preencher o RecyclerView com dados dos clientes
 */
public class AdapterClients extends RecyclerView.Adapter<AdapterClients.MyViewHolder> {
    private final ClientsFragment fragment;

    private final List<Client> clients;

    /**
     * Construtor da classe
     *
     * @param fragment Fragmento associado ao adaptador
     * @param clients  Lista de clientes
     */
    public AdapterClients(ClientsFragment fragment, List<Client> clients) {
        this.fragment = fragment;
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_clients_list, parent, false);

        return new MyViewHolder(itemView);
    }

    /**
     * Vincula os dados do cliente à um item no RecyclerView
     *
     * @param holder   Instância de MyViewHolder
     * @param position Posição do item
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Client client = clients.get(position);

        // Configura o nome do cliente
        holder.textClientName.setText(client.getName());

        // Configura as informações do cliente caso nao cadastrado exibe "Não cadastrado"
        String clientInfo = client.getCpf_cnpj().isEmpty() ? "Não cadastrado" : client.getCpf_cnpj();
        holder.textClientInfo.setText(clientInfo);

        // Configura OnClickListener exclusão do cliente
        holder.imageDeleteClient.setOnClickListener(view -> {
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
                    .setMessage("Deletar: " + client.getName())
                    .setPositiveButton(positiveButton, (dialogInterface, i) -> {
                        if (new ClientDAO(view.getContext()).delete(client)) {
                            fragment.getRegisters();
                            MainActivity.emitUpdate();
                        } else {
                            Toast.makeText(view.getContext(), "Erro ao deletar cliente", Toast.LENGTH_LONG).show();
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
        return clients.size();
    }

    /**
     * Classe interna de uma View de item
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textClientName;
        private final TextView textClientInfo;
        private final ImageView imageDeleteClient;

        /**
         * Construtorda classe interna
         *
         * @param itemView View de um item
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textClientName = itemView.findViewById(R.id.textClientsName);
            textClientInfo = itemView.findViewById(R.id.textClientsInfo);
            imageDeleteClient = itemView.findViewById(R.id.imageDeletCliente);
        }
    }
}

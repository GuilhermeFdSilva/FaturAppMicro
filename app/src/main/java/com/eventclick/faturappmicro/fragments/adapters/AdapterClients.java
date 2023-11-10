package com.eventclick.faturappmicro.fragments.adapters;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.ClientsFragment;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.ClientDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.util.List;

public class AdapterClients extends RecyclerView.Adapter<AdapterClients.MyViewHolder> {
    private Context context;
    private ClientsFragment fragment;

    private List<Client> clients;

    public AdapterClients(Context context, ClientsFragment fragment, List<Client> clients) {
        this.context = context;
        this.fragment = fragment;
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
        Client client = clients.get(position);

        holder.textClientName.setText(client.getName());

        String clientInfo = client.getCpf_cnpj().isEmpty() ? "Não cadastrado" : client.getCpf_cnpj();
        holder.textClientInfo.setText(clientInfo);

        holder.imageDeleteClient.setOnClickListener(new View.OnClickListener() {
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
                        .setMessage("Deletar: " + client.getName())
                        .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (new ClientDAO(view.getContext()).delete(client)) {
                                    fragment.getRegisters();
                                    MainActivity.emitUpdate();
                                } else {
                                    Toast.makeText(view.getContext(), "Erro ao deletar cliente", Toast.LENGTH_LONG);
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
        return clients.size();
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

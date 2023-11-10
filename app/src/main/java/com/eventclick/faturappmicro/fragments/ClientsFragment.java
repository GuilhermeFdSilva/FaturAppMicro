package com.eventclick.faturappmicro.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.adapters.AdapterClients;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.ClientDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;
import com.eventclick.faturappmicro.helpers.filters.filterCpfCnpj;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {
    private ClientDAO clientDAO;

    private List<Client> clients;
    private AdapterClients adapter;

    private RecyclerView recyclerView;
    private TextView textEmptyClients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        clientDAO = new ClientDAO(getContext());

        recyclerView = view.findViewById(R.id.recyclerClients);
        textEmptyClients = view.findViewById(R.id.textEmptyClients);

        getRegisters();

        view.findViewById(R.id.fabClients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddClient();
            }
        });

        return view;
    }

    public void getRegisters() {
        clients = clientDAO.list();

        adapter = new AdapterClients(getContext(), this, clients);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if (clients.isEmpty()) {
            textEmptyClients.setVisibility(View.VISIBLE);
        } else {
            textEmptyClients.setVisibility(View.GONE);
        }
    }

    private void openAddClient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int greenColor = ContextCompat.getColor(getContext(), R.color.primaryDark);
        int redColor = ContextCompat.getColor(getContext(), R.color.red);

        SpannableString positiveButton = new SpannableString("Cadastrar");
        positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString negativeButton = new SpannableString("Cancelar");
        negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

        EditText editCpfCnpj = view.findViewById(R.id.editTextCpf_Cnpj);
        editCpfCnpj.addTextChangedListener(new filterCpfCnpj(editCpfCnpj));

        builder.setView(view)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Client client = bind(view);

                        if (client == null) {
                            Toast.makeText(getContext(), "O campo \"NOME\" é obrigatorio", Toast.LENGTH_LONG).show();
                        } else {
                            if (clientDAO.save(client)) {
                                Toast.makeText(getContext(), "Cliente cadastrado", Toast.LENGTH_LONG).show();
                                getRegisters();
                                MainActivity.emitUpdate();
                            } else {
                                Toast.makeText(getContext(), "Erro ao cadastrar cliente", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(negativeButton, null)
                .create()
                .show();
    }

    private Client bind(View view) {
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextCpfCnpj = view.findViewById(R.id.editTextCpf_Cnpj);
        EditText editTextContact = view.findViewById(R.id.editTextContact);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);
        EditText editTextPix = view.findViewById(R.id.editTextPix);

        if (editTextName.getText().toString().isEmpty()) {
            return null;
        }

        String name = editTextName.getText().toString();
        String cpfCnpj = editTextCpfCnpj.getText().toString();
        String contact = editTextContact.getText().toString();
        String address = editTextAddress.getText().toString();
        String pix = editTextPix.getText().toString();

        return new Client(name, cpfCnpj, contact, address, pix);
    }
}
package com.eventclick.faturappmicro.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.adapters.AdapterAccounts;
import com.eventclick.faturappmicro.fragments.adapters.AdapterSpinner;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.ClientDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class AccountsFragment extends Fragment {
    private AccountDAO accountDAO;
    private ClientDAO clientDAO;

    private List<Account> accounts;
    private List<Client> clients;
    private AdapterAccounts adapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        accountDAO = new AccountDAO(getContext());
        clientDAO = new ClientDAO(getContext());

        recyclerView = view.findViewById(R.id.recyclerAccounts);

        getRegisters();

        view.findViewById(R.id.fabAccounts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccounts();
            }
        });

        return view;
    }

    public void getRegisters() {
        accounts = accountDAO.list();
        clients = clientDAO.list();
        clients.add(0, new Client(-1L, "- Selecione um item -"));
        adapter = new AdapterAccounts(getContext(), this, accounts, clients);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void addAccounts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int greenColor = ContextCompat.getColor(getContext(), R.color.primaryDark);
        int redColor = ContextCompat.getColor(getContext(), R.color.red);

        SpannableString positiveButton = new SpannableString("Cadastrar");
        positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString negativeButton = new SpannableString("Cancelar");
        negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_account, null);

        Spinner spinner = view.findViewById(R.id.spinnerClient);
        spinner.setAdapter(new AdapterSpinner(getContext(), clients));

        builder.setView(view)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Account account = bind(view);

                        if (account == null) {
                            Toast.makeText(getContext(), "O campo \"DESCRIÇÃO\" e \"VALOR\" são obrigatorios", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Conta cadastrada", Toast.LENGTH_LONG).show();
                            accountDAO.save(account);
                            getRegisters();
                        }
                    }
                })
                .setNegativeButton(negativeButton, null)
                .create()
                .show();
    }

    private Account bind(View view) {
        EditText viewDescription = view.findViewById(R.id.editTextDescription);
        EditText viewValue = view.findViewById(R.id.editTextValue);
        Switch viewPaid = view.findViewById(R.id.switchPaid);
        Spinner viewClient = view.findViewById(R.id.spinnerClient);
        EditText viewExpiration = view.findViewById(R.id.editTextExpiration);

        if (viewDescription.getText().toString().isEmpty() || viewValue.getText().toString().isEmpty()) {
            return null;
        }

        String description = viewDescription.getText().toString();
        double value = Double.parseDouble(viewValue.getText().toString());
        boolean paid = viewPaid.isActivated();
        Long clientId = viewClient.getSelectedItemId() == -1L ? null : viewClient.getSelectedItemId();
        java.sql.Date expiration = new Date(new java.util.Date().getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = dateFormat.parse(viewExpiration.getText().toString());
            expiration = new java.sql.Date(date.getTime());
        } catch (Exception e) {
            Log.i("DATA FORMAT", "error at converting date");
        }

        Account account = new Account(clientId, description, value, expiration, paid);

        return account;
    }
}
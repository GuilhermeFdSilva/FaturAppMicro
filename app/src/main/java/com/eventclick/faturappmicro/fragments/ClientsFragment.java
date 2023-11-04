package com.eventclick.faturappmicro.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.adapters.AdapterClients;

import java.util.ArrayList;

public class ClientsFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        recyclerView = view.findViewById(R.id.recyclerClients);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        AdapterClients adapter = new AdapterClients(new ArrayList<>());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.fabClients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClient();
            }
        });

        return view;
    }

    private void addClient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        int greenColor = ContextCompat.getColor(getContext(), R.color.primaryDark);
        int redColor = ContextCompat.getColor(getContext(), R.color.red);

        SpannableString positiveButton = new SpannableString("Cadastrar");
        positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString negativeButton = new SpannableString("Cancelar");
        negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

        builder.setView(view)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(negativeButton, null)
                .create()
                .show();
    }
}
package com.eventclick.faturappmicro.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.helpers.UserPreferences;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.filters.filterCpfCnpj;
import com.eventclick.faturappmicro.helpers.observers.ObserveFragment;

import java.util.List;

public class CashierFragment extends Fragment implements ObserveFragment {

    private UserPreferences preferences;
    private InputMethodManager inputMethodManager;

    private AccountDAO accountDAO;

    private TextView textCompanyName, textCompanyCnpj, textCashier, textEntranceValue, textExitValue;
    private EditText inputName, inputCnpj;
    private ImageView imageEditName, imageSaveName, imageEditCnpj, imageCopyCnpj, imageSaveCnpj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashier, container, false);

        preferences = new UserPreferences(getContext());
        inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        accountDAO = new AccountDAO(getContext());
        MainActivity.registerObserver(this);

        setViews(view);

        textCompanyName.setText(preferences.getPreference(preferences.KEY_NAME));
        textCompanyCnpj.setText(preferences.getPreference(preferences.KEY_CNPJ));

        imageEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCompanyName();
            }
        });
        imageEditCnpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCompanyCnpj();
            }
        });

        imageSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCompanyName();
            }
        });
        imageSaveCnpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCompanyCnpj();
            }
        });

        imageCopyCnpj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyCompanyCnpj(getContext());
            }
        });

        configureViewsNameVisibility();
        configureViewsCnpjVisibility();

        return view;
    }

    private void setViews(View view) {
        textCompanyName = view.findViewById(R.id.textCompanyName);
        textCompanyCnpj = view.findViewById(R.id.textCompanyCnpj);
        inputName = view.findViewById(R.id.inputName);
        inputCnpj = view.findViewById(R.id.inputCnpj);
        imageEditName = view.findViewById(R.id.imageEditName);
        imageSaveName = view.findViewById(R.id.imageSaveName);
        imageEditCnpj = view.findViewById(R.id.imageEditCnpj);
        imageCopyCnpj = view.findViewById(R.id.imageCopyCnpj);
        imageSaveCnpj = view.findViewById(R.id.imageSaveCnpj);

        textCashier = view.findViewById(R.id.textCashier);
        textEntranceValue = view.findViewById(R.id.textEntranceValue);
        textExitValue = view.findViewById(R.id.textExitValue);

        getValues();

        inputCnpj.addTextChangedListener(new filterCpfCnpj(inputCnpj));
    }

    private void configureViewsNameVisibility() {
        textCompanyName.setVisibility(View.VISIBLE);
        imageEditName.setVisibility(View.VISIBLE);

        inputName.setVisibility(View.GONE);
        imageSaveName.setVisibility(View.GONE);

        inputName.clearFocus();
        inputMethodManager.hideSoftInputFromWindow((IBinder) inputName.getWindowToken(), 0);

    }

    private void configureViewsCnpjVisibility() {
        textCompanyCnpj.setVisibility(View.VISIBLE);
        imageEditCnpj.setVisibility(View.VISIBLE);
        imageCopyCnpj.setVisibility(View.VISIBLE);

        inputCnpj.setVisibility(View.GONE);
        imageSaveCnpj.setVisibility(View.GONE);

        inputCnpj.clearFocus();
        inputMethodManager.hideSoftInputFromWindow((IBinder) inputCnpj.getWindowToken(), 0);

    }

    private void editCompanyName() {
        textCompanyName.setVisibility(View.GONE);
        imageEditName.setVisibility(View.GONE);
        inputName.setVisibility(View.VISIBLE);
        imageSaveName.setVisibility(View.VISIBLE);

        inputName.setText(preferences.getPreference(preferences.KEY_NAME));
        inputName.requestFocus();
        inputMethodManager.showSoftInput(inputName, InputMethodManager.SHOW_IMPLICIT);
    }

    private void editCompanyCnpj() {
        textCompanyCnpj.setVisibility(View.GONE);
        imageEditCnpj.setVisibility(View.GONE);
        imageCopyCnpj.setVisibility(View.GONE);
        inputCnpj.setVisibility(View.VISIBLE);
        imageSaveCnpj.setVisibility(View.VISIBLE);

        inputCnpj.setText(preferences.getPreference(preferences.KEY_CNPJ));
        inputCnpj.requestFocus();
        inputMethodManager.showSoftInput(inputCnpj, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setCompanyName() {
        String newCompanyName = inputName.getText().toString();

        preferences.save(preferences.KEY_NAME,
                newCompanyName.isEmpty() ? getString(R.string.empty_company_name) : newCompanyName);

        textCompanyName.setText(preferences.getPreference(preferences.KEY_NAME));
        configureViewsNameVisibility();
    }

    private void setCompanyCnpj() {
        String newCompanyCnpj = inputCnpj.getText().toString();

        preferences.save(preferences.KEY_CNPJ,
                newCompanyCnpj.isEmpty() ? getString(R.string.empty_company_cpf_cnpj) : newCompanyCnpj);
        textCompanyCnpj.setText(preferences.getPreference(preferences.KEY_CNPJ));
        configureViewsCnpjVisibility();
    }

    private void copyCompanyCnpj(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Cnpj", preferences.getPreference(preferences.KEY_CNPJ));
        clipboard.setPrimaryClip(data);
    }

    private void getValues() {
        List<Account> accounts = accountDAO.list();

        double value = accounts.stream().mapToDouble(Account::getValue).sum();
        textCashier.setText(String.format("R$ %.2f", value));

        if (value < 0){
            int redColor = ContextCompat.getColor(getContext(), R.color.red);
            textCashier.setTextColor(redColor);
        }
    }

    @Override
    public void update() {
        getValues();
    }
}
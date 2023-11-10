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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.filters.filterCpfCnpj;
import com.eventclick.faturappmicro.helpers.observers.ObserveFragment;
import com.eventclick.faturappmicro.helpers.preferences.UserPreferences;

import java.time.LocalDate;
import java.util.List;

/**
 * Fragment inicial que exibe o caixa, resultados do mês e informações pessoais
 */
public class CashierFragment extends Fragment implements ObserveFragment {
    // Objeto para acessar as preferências do usuário
    private UserPreferences preferences;
    // Gerenciador e controlador do teclado virtual
    private InputMethodManager inputMethodManager;

    private AccountDAO accountDAO;

    private TextView textCompanyName, textCompanyCnpj, textCashier, textEntranceValue, textExitValue;
    private EditText inputName, inputCnpj;
    private ImageView imageEditName, imageSaveName, imageEditCnpj, imageCopyCnpj, imageSaveCnpj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout
        View view = inflater.inflate(R.layout.fragment_cashier, container, false);

        // Inicializa as preferências do usuário e o gerenciador do teclado
        preferences = new UserPreferences(getContext());
        inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Inicializa a DAO para operações no banco de dados
        accountDAO = new AccountDAO(getContext());
        // Registra essa fragment como observadora da MainActivity
        MainActivity.registerObserver(this);

        setViews(view);

        // Configuração dos métodos OnClickListener dos botões
        imageEditName.setOnClickListener(view1 -> editCompanyName());
        imageEditCnpj.setOnClickListener(view12 -> editCompanyCnpj());

        imageSaveName.setOnClickListener(view13 -> setCompanyName());
        imageSaveCnpj.setOnClickListener(view14 -> setCompanyCnpj());

        imageCopyCnpj.setOnClickListener(view15 -> copyCompanyCnpj(getContext()));

        configureViewsNameVisibility();
        configureViewsCnpjVisibility();

        return view;
    }

    /**
     * Método responsavel por fazer o Binding das Views do Fragment
     *
     * @param view View do Fragment
     */
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

        // Busca as preferências do usuário e verifica se estão vazias ou não preenchidas
        textCompanyName.setText(preferences.getPreference(UserPreferences.KEY_NAME).isEmpty() ?
                getText(R.string.empty_company_name) :
                preferences.getPreference(UserPreferences.KEY_NAME));
        textCompanyCnpj.setText(preferences.getPreference(UserPreferences.KEY_CNPJ).isEmpty() ?
                getText(R.string.empty_company_cpf_cnpj) :
                preferences.getPreference(UserPreferences.KEY_CNPJ));

        getValues();

        // Adiciona o filtro de CPF ou CNPJ
        inputCnpj.addTextChangedListener(new filterCpfCnpj(inputCnpj));

    }

    /**
     * Método responsavel por configurar a visibilidade dos TextView do nome do usuário
     */
    private void configureViewsNameVisibility() {
        textCompanyName.setVisibility(View.VISIBLE);
        imageEditName.setVisibility(View.VISIBLE);

        inputName.setVisibility(View.GONE);
        imageSaveName.setVisibility(View.GONE);

        // Remove o foco do Input
        inputName.clearFocus();
        // Desativa o teclado virtual
        inputMethodManager.hideSoftInputFromWindow((IBinder) inputName.getWindowToken(), 0);
    }

    /**
     * Método responsavel por configurar a visibilidade dos TextView do CPF ou CNPJ do usuário
     */
    private void configureViewsCnpjVisibility() {
        textCompanyCnpj.setVisibility(View.VISIBLE);
        imageEditCnpj.setVisibility(View.VISIBLE);
        imageCopyCnpj.setVisibility(View.VISIBLE);

        inputCnpj.setVisibility(View.GONE);
        imageSaveCnpj.setVisibility(View.GONE);

        // Remove o foco do Input
        inputCnpj.clearFocus();
        // Desativa o teclado virtual
        inputMethodManager.hideSoftInputFromWindow((IBinder) inputCnpj.getWindowToken(), 0);

    }

    /**
     * Método que abre o campo de input para edição do nome do usuário
     */
    private void editCompanyName() {
        textCompanyName.setVisibility(View.GONE);
        imageEditName.setVisibility(View.GONE);
        inputName.setVisibility(View.VISIBLE);
        imageSaveName.setVisibility(View.VISIBLE);

        // Configura a preferencia antiga para edição
        inputName.setText(preferences.getPreference(UserPreferences.KEY_NAME));
        // Foca o campo de Input
        inputName.requestFocus();
        inputName.setSelection(preferences.getPreference(UserPreferences.KEY_NAME).length());
        // Exibe o teclado virtual
        inputMethodManager.showSoftInput(inputName, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Método que abre o campo de input para edição do CPF ou CNPJ do usuário
     */
    private void editCompanyCnpj() {
        textCompanyCnpj.setVisibility(View.GONE);
        imageEditCnpj.setVisibility(View.GONE);
        imageCopyCnpj.setVisibility(View.GONE);
        inputCnpj.setVisibility(View.VISIBLE);
        imageSaveCnpj.setVisibility(View.VISIBLE);

        // Configura a preferencia antiga para edição
        inputCnpj.setText(preferences.getPreference(UserPreferences.KEY_CNPJ));
        // Foca o campo de Input
        inputCnpj.requestFocus();
        inputCnpj.setSelection(preferences.getPreference(UserPreferences.KEY_CNPJ).length());
        // Exibe o teclado virtual
        inputMethodManager.showSoftInput(inputCnpj, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Método responsável por salvar a novo nome do usuário
     */
    private void setCompanyName() {
        String newCompanyName = inputName.getText().toString();

        preferences.save(UserPreferences.KEY_NAME, newCompanyName);

        // Verifica se o campo foi preenchido e configura o novo nome de acordo
        textCompanyName.setText(preferences.getPreference(UserPreferences.KEY_NAME).isEmpty() ?
                getText(R.string.empty_company_name) :
                preferences.getPreference(UserPreferences.KEY_NAME));

        configureViewsNameVisibility();
    }

    /**
     * Método responsável por salvar a novo CPF ou CNOJ do usuário
     */
    private void setCompanyCnpj() {
        String newCompanyCnpj = inputCnpj.getText().toString();

        preferences.save(UserPreferences.KEY_CNPJ, newCompanyCnpj);

        // Verifica se o campo foi preenchido e configura o novo CPF ou CNPJ de acordo
        textCompanyCnpj.setText(preferences.getPreference(UserPreferences.KEY_CNPJ).isEmpty() ?
                getText(R.string.empty_company_cpf_cnpj) :
                preferences.getPreference(UserPreferences.KEY_CNPJ));
        configureViewsCnpjVisibility();
    }

    /**
     * Método que copia para área de tranferencia o CPF ou CNPJ do usuário
     *
     * @param context Contexto da aplicação
     */
    private void copyCompanyCnpj(Context context) {
        /*
            Verifica se as preferencias estão vazias, caso estejam, nao copia nada e exibe a mensagem negativa
            caso esteja copia copia o CPF ou CNPJ cadastrado
         */
        if (!preferences.getPreference(UserPreferences.KEY_CNPJ).isEmpty() &&
                (preferences.getPreference(UserPreferences.KEY_CNPJ).equals(getString(R.string.empty_company_cpf_cnpj)))) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData
                    .newPlainText("Cnpj", preferences.getPreference(UserPreferences.KEY_CNPJ));
            clipboard.setPrimaryClip(data);
            Toast.makeText(getContext(), "Copiado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Nada para copiar ¯\\_(ツ)_/¯", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método responsavel por obter os valores de entrada e sáida do mês, assim como o total do caixa
     */
    private void getValues() {
        List<Account> accounts = accountDAO.list();

        // Soma todas as contas ja pagas
        double value = accounts.stream().mapToDouble(account -> account.isPaid() ? account.getValue() : 0).sum();
        textCashier.setText(String.format("R$ %.2f", value));

        /*
            Verifica se o valor total é menor que zero, caso seja aplica a cor vermelha
            Caso contrario, aplica a cor verde
         */
        if (value < 0) {
            int redColor = ContextCompat.getColor(getContext(), R.color.red);
            textCashier.setTextColor(redColor);
        } else {
            int greenColor = ContextCompat.getColor(getContext(), R.color.primaryDark);
            textCashier.setTextColor(greenColor);
        }

        // Data atual para verificação das entradas e saaidas do mês
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        // Valores positivos filtrados pela data atual
        double positiveValues = accounts.stream()
                .filter(account ->
                        account.getPaidAt().getMonth() + 1 == currentMonth &&
                                account.getPaidAt().getYear() + 1900 == currentYear &&
                                account.isPaid() &&
                                account.getValue() > 0)
                .mapToDouble(Account::getValue)
                .sum();

        textEntranceValue.setText(String.format("R$ %.2f", positiveValues));

        // Valores negativos filtrados pela data atual
        double negativeValues = accounts.stream()
                .filter(account ->
                        account.getPaidAt().getMonth() + 1 == currentMonth &&
                                account.getPaidAt().getYear() + 1900 == currentYear &&
                                account.isPaid() &&
                                account.getValue() < 0)
                .mapToDouble(Account::getValue)
                .sum();

        textExitValue.setText(String.format("R$ %.2f", negativeValues));
    }

    /**
     * Método chamado quando a atualização emitida pelo Observable
     */
    @Override
    public void update() {
        getValues();
    }
}
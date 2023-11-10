package com.eventclick.faturappmicro.fragments;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eventclick.faturappmicro.MainActivity;
import com.eventclick.faturappmicro.R;
import com.eventclick.faturappmicro.fragments.adapters.AdapterAccounts;
import com.eventclick.faturappmicro.fragments.adapters.AdapterSpinner;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.AccountDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.DAO.ClientDAO;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Account;
import com.eventclick.faturappmicro.helpers.dbHelpers.models.Client;
import com.eventclick.faturappmicro.helpers.filters.filterDate;
import com.eventclick.faturappmicro.helpers.observers.ObserveFragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Fragment responsável pela exibição das contas
 */
public class AccountsFragment extends Fragment implements ObserveFragment {
    private AccountDAO accountDAO;
    private ClientDAO clientDAO;

    private List<Client> clients;

    // RecyclerView para exibir as contas
    private RecyclerView recyclerView;
    // TextView para ser mostrado quando a lista estiver vazia
    private TextView textEmptyAccounts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        // Inicializa as DAOs para operações no banco de dados
        accountDAO = new AccountDAO(getContext());
        clientDAO = new ClientDAO(getContext());

        // Binding Views
        recyclerView = view.findViewById(R.id.recyclerAccounts);
        textEmptyAccounts = view.findViewById(R.id.textEmptyAccounts);

        getRegisters();

        // Configura um listener para o botão de adicionar conta
        view.findViewById(R.id.fabAccounts).setOnClickListener(view1 -> openAddAccounts());

        return view;
    }

    /**
     * Método responsável por obter a lista de contas através da DAO;
     * configurar o adapter da lista de contas;
     * e gerenciar a visibilidade da mensagem de lista vazia
     */
    public void getRegisters() {
        // Obté a lista de contas
        List<Account> accounts = accountDAO.list();
        // Obtém a lista de clientes para cadastro de contas
        clients = clientDAO.list();
        // Adiciona um cliente vazio
        clients.add(0, new Client(-1L, "- Selecione um item -"));

        // Configura o adapter com a lista de contas
        AdapterAccounts adapter = new AdapterAccounts(getContext(), this, accounts, clients);

        // Define a orientação do recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        // Caso a lista esteja vazia configura a visibilidade da mensagem como VISIBLE, caso contrario, GONE
        if (accounts.isEmpty()){
            textEmptyAccounts.setVisibility(View.VISIBLE);
        } else {
            textEmptyAccounts.setVisibility(View.GONE);
        }
    }

    /**
     * Método responsável por criar a AlertDialog de cadastro de contas, inflando a view de cadastro;
     * também configura os botões de ação da dialog para salvar um nova conta
     */
    private void openAddAccounts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Obtendo as cores do R.color
        int greenColor = ContextCompat.getColor(getContext(), R.color.primaryDark);
        int redColor = ContextCompat.getColor(getContext(), R.color.red);

        // Configura uma String com cor definida verde
        SpannableString positiveButton = new SpannableString("Cadastrar");
        positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Configura uma String com cor definida vermelha
        SpannableString negativeButton = new SpannableString("Cancelar");
        negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Infla o layout de cadastro de cliente
        View view = getLayoutInflater().inflate(R.layout.dialog_add_account, null);

        // Configura o botão de conta a PAGAR ou a RECEBER
        ToggleButton toggleButton = view.findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                toggleButton.setBackgroundColor(getContext().getColor(R.color.accent));
            } else {
                toggleButton.setBackgroundColor(getContext().getColor(R.color.redAccent));
            }
        });

        // Configura o adapter para o Spinner de clientes
        Spinner spinner = view.findViewById(R.id.spinnerClient);
        spinner.setAdapter(new AdapterSpinner(getContext(), clients));

        // Configura as teclas aceitas no input de valor
        EditText value = view.findViewById(R.id.editTextValue);
        value.setKeyListener(DigitsKeyListener.getInstance("0123456789,"));

        // Configura o filtro de data
        EditText editExpiration = view.findViewById(R.id.editTextExpiration);
        editExpiration.addTextChangedListener(new filterDate(editExpiration));

        // Configuração do AlertDialog
        builder.setView(view)
                .setPositiveButton(positiveButton, (dialogInterface, i) -> {
                    Account account = bind(view);

                    // Caso o usuário não tenha preenchido um campo obrigatorio, exibe mensagem e não realiza o cadastro
                    if (account == null) {
                        Toast.makeText(getContext(), "Os campos \"DESCRIÇÃO\" e \"VALOR\" são obrigatorios", Toast.LENGTH_LONG).show();
                    } else {
                        // Caso a operação seja bem sucedida mostra mensagem de sucesso, caso contrario informa o erro
                        if (accountDAO.save(account)){
                            Toast.makeText(getContext(), "Conta cadastrada", Toast.LENGTH_LONG).show();
                            getRegisters();
                            MainActivity.emitUpdate();
                        } else {
                            Toast.makeText(getContext(), "Erro ao cadastrar conta", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(negativeButton, null)
                .create()
                .show();
    }

    /**
     * Método responsável por obter os dados dos EditText para criação de um objeto conta
     *
     * @param view View da Dialog de cadastro de conta
     * @return Objeto account caso os campos estejam corretos, caso contrario retorna NULL
     */
    private Account bind(View view) {
        ToggleButton toggleButton = view.findViewById(R.id.toggleButton);
        EditText viewDescription = view.findViewById(R.id.editTextDescription);
        EditText viewValue = view.findViewById(R.id.editTextValue);
        Switch viewPaid = view.findViewById(R.id.switchPaid);
        Spinner viewClient = view.findViewById(R.id.spinnerClient);
        EditText viewExpiration = view.findViewById(R.id.editTextExpiration);

        // Verifica se os campos obrigatorios estão preenchidos
        if (viewDescription.getText().toString().isEmpty() ||
                viewValue.getText().toString().isEmpty()) {
            return null;
        }

        String description = viewDescription.getText().toString();
        // verifica se o valor é positivo ou negativo de acorto com o toggleButton
        double value = toggleButton.isChecked() ?
                Double.parseDouble(viewValue.getText().toString().replace(",", ".")) :
                Double.parseDouble("-" + viewValue.getText().toString().replace(",", "."));
        boolean paid = viewPaid.isChecked();
        Long clientId = viewClient.getSelectedItemId() == -1L ? null : viewClient.getSelectedItemId();
        java.sql.Date expiration = new Date(new java.util.Date().getTime());

        // Caso o campo de vencimento esteja preenchodo, tenta colocar data de vencimento
        if (!viewExpiration.getText().toString().isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                java.util.Date date = dateFormat.parse(viewExpiration.getText().toString());
                expiration = new java.sql.Date(date.getTime());
            } catch (Exception e) {
                Log.i("DATA FORMAT", "error at converting date");
            }
        }

        Account account;

        // Caso a conta ja esteja paga, coloca a data de pagamento como a mesma data de validade
        if (paid){
            account = new Account(clientId, description, value, expiration, expiration, true);
        } else {
            account = new Account(clientId, description, value, expiration, false);
        }

        return account;
    }

    /**
     * Método chamado quando a atualização emitida pelo Observable
     */
    @Override
    public void update() {
        clients = clientDAO.list();
    }
}
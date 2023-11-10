package com.eventclick.faturappmicro.fragments;

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

import java.util.List;

/**
 * Fragment responsável pela exibição dos clientes
 */
public class ClientsFragment extends Fragment {
    private ClientDAO clientDAO;

    // RecyclerView para exibir os clientes
    private RecyclerView recyclerView;
    // TextView para ser mostrado quando a lista estiver vazia
    private TextView textEmptyClients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        // Inicializa a DAO para operações no banco de dados
        clientDAO = new ClientDAO(getContext());

        // Binding views
        recyclerView = view.findViewById(R.id.recyclerClients);
        textEmptyClients = view.findViewById(R.id.textEmptyClients);

        getRegisters();

        // Configura um listener para o botão de adicionar cliente
        view.findViewById(R.id.fabClients).setOnClickListener(view1 -> openAddClient());

        return view;
    }

    /**
     * Método responsável por obter a lista de clientes através da DAO;
     * configurar o adapter da lista de clientes;
     * e gerenciar a visibilidade da mensagem de lista vazia
     */
    public void getRegisters() {
        // Obtém a lista de clientes
        List<Client> clients = clientDAO.list();

        // Configura o adapter com a lista de clientes
        AdapterClients adapter = new AdapterClients(getContext(), this, clients);

        // Define a orientação do recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        // Caso a lista esteja vazia configura a visibilidade da mensagem como VISIBLE, caso contrario, GONE
        if (clients.isEmpty()) {
            textEmptyClients.setVisibility(View.VISIBLE);
        } else {
            textEmptyClients.setVisibility(View.GONE);
        }
    }

    /**
     * Método responsável por criar a AlertDialog de cadastro de clientes, inflando a view de cadastro;
     * também configura os botões de ação da dialog para salvar um novo cliente
     */
    private void openAddClient() {
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
        View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

        // Configura o filtro de CPF ou CNPJ
        EditText editCpfCnpj = view.findViewById(R.id.editTextCpf_Cnpj);
        editCpfCnpj.addTextChangedListener(new filterCpfCnpj(editCpfCnpj));

        // Configuração do AlertDialog
        builder.setView(view)
                .setPositiveButton(positiveButton, (dialogInterface, i) -> {
                    Client client = bind(view);

                    // Caso o cliente nao tenha preenchido um campo obrigatorio, exibe mensagem e nao realiza o cadastro
                    if (client == null) {
                        Toast.makeText(getContext(), "O campo \"NOME\" é obrigatorio", Toast.LENGTH_LONG).show();
                    } else {
                        // Caso a operação seja bem sucedida mostra mensagem de sucesso, caso contrario informa o erro
                        if (clientDAO.save(client)) {
                            Toast.makeText(getContext(), "Cliente cadastrado", Toast.LENGTH_LONG).show();
                            getRegisters();
                            MainActivity.emitUpdate();
                        } else {
                            Toast.makeText(getContext(), "Erro ao cadastrar cliente", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(negativeButton, null)
                .create()
                .show();
    }

    /**
     * Método responsável por obter os dados dos EditText para criação de um objeto cliente
     *
     * @param view View da Dialog de cadastro de cliente
     * @return Objeto cliente caso os campos estejam corretos, caso contrario retorna NULL
     */
    private Client bind(View view) {
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextCpfCnpj = view.findViewById(R.id.editTextCpf_Cnpj);
        EditText editTextContact = view.findViewById(R.id.editTextContact);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);
        EditText editTextPix = view.findViewById(R.id.editTextPix);

        // Verifica se os campos obrigatorios estão preenchidos
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
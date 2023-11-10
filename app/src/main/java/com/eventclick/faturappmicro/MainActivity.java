package com.eventclick.faturappmicro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.eventclick.faturappmicro.databinding.ActivityMainBinding;
import com.eventclick.faturappmicro.fragments.AccountsFragment;
import com.eventclick.faturappmicro.fragments.CashierFragment;
import com.eventclick.faturappmicro.fragments.ClientsFragment;
import com.eventclick.faturappmicro.helpers.observers.ObserveFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Lista de observadores utilizada para monitorar mudanças no banco de dados
    private static final List<ObserveFragment> OBSERVE_FRAGMENTS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.eventclick.faturappmicro.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Binding de views
        SmartTabLayout smartTabLayout = findViewById(R.id.viewPagerTab);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Configuração do adapter para o viewPager
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Clientes", ClientsFragment.class)
                .add("Caixa", CashierFragment.class)
                .add("Contas", AccountsFragment.class)
                .create()
        );

        // Set do adapter e do viewPager
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
        // Define a segunda aba como inicial
        viewPager.setCurrentItem(1);
    }

    /**
     * Registra um observador
     *
     * @param observeFragment Observador a ser registrado
     */
    public static void registerObserver(ObserveFragment observeFragment) {
        OBSERVE_FRAGMENTS.add(observeFragment);
    }

    /**
     * Notifica os observadores para atualizarem seus dados
     */
    public static void emitUpdate() {
        for (ObserveFragment observer :OBSERVE_FRAGMENTS) {
            observer.update();
        }
    }
}
package com.eventclick.faturappmicro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.eventclick.faturappmicro.databinding.ActivityMainBinding;
import com.eventclick.faturappmicro.fragments.AccountsFragment;
import com.eventclick.faturappmicro.fragments.CashierFragment;
import com.eventclick.faturappmicro.fragments.ClientsFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
          getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Clientes", ClientsFragment.class)
                .add("Caixa", CashierFragment.class)
                .add("Contas", AccountsFragment.class)
                .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }
}
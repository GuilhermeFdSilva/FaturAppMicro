package com.eventclick.faturappmicro;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    private int currentPosition = 1;

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (position == 0) {
                    binding.fab.setVisibility(View.VISIBLE);
                    binding.fab.setImageResource(R.drawable.ic_add_client);
                } else if (position == 1) {
                    binding.fab.setVisibility(View.GONE);
                } else if (position == 2) {
                    binding.fab.setVisibility(View.VISIBLE);
                    binding.fab.setImageResource(R.drawable.ic_add_account);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPosition == 0) {
                    Toast.makeText(getApplicationContext(), "Adiciona cliente", Toast.LENGTH_SHORT).show();
                } else if (currentPosition == 2) {
                    Toast.makeText(getApplicationContext(), "Adiciona conta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
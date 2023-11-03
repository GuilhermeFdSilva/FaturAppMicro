package com.eventclick.faturappmicro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
                addData();
            }
        });
    }

    private void addData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int greenColor = ContextCompat.getColor(this, R.color.primaryDark);
        int redColor = ContextCompat.getColor(this, R.color.red);

        SpannableString positiveButton = new SpannableString("Cadastrar");
        positiveButton.setSpan(new ForegroundColorSpan(greenColor), 0, positiveButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString negativeButton = new SpannableString("Cancelar");
        negativeButton.setSpan(new ForegroundColorSpan(redColor), 0, negativeButton.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (currentPosition == 0) {
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
        } else if (currentPosition == 2) {
            View view = getLayoutInflater().inflate(R.layout.dialog_add_account, null);

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
}
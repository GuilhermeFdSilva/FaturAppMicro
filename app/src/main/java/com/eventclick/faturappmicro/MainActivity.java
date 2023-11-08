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
import com.eventclick.faturappmicro.helpers.observers.ObserveFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    private static final List<ObserveFragment> OBSERVE_FRAGMENTS = new ArrayList<>();

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

    public static void registerObserver(ObserveFragment observeFragment) {
        OBSERVE_FRAGMENTS.add(observeFragment);
    }

    public static void emitUpdate() {
        for (ObserveFragment observer :OBSERVE_FRAGMENTS) {
            observer.update();
        }
    }
}
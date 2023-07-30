package com.cryptowatch.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cryptowatch.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
    }

    protected void initComponents() {
        BottomNavigationView nav = findViewById(R.id.navbar);
        AppBarConfiguration abc = new AppBarConfiguration.Builder(R.id.market, R.id.portfolio, R.id.search, R.id.news).build();
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragment);
        NavController nvc = nhf.getNavController();
        NavigationUI.setupActionBarWithNavController(this, nvc, abc);
        NavigationUI.setupWithNavController(nav, nvc);
    }
}
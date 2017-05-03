package whatsapp.google.com.whatsapp.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.adapter.TabAdapter;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Toolbar toolbar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth        = FirebaseConnection.getFirebaseAuth();
        toolbar     = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        //Configurar Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager_pagina);
        viewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

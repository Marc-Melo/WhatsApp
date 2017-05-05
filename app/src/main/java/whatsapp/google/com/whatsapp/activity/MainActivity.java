package whatsapp.google.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.adapter.TabAdapter;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Contato;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference referenciaFirebase;


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
            case R.id.item_adicionar:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void abrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }else {
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    referenciaFirebase = FirebaseConnection.getFirebaseReference().child("usuarios").child(identificadorContato);
                    referenciaFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                Usuario usuarioContato = dataSnapshot.getValue( Usuario.class );

                                Preferencias preferencias = new Preferencias(getApplicationContext());
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                referenciaFirebase = FirebaseConnection.getFirebaseReference();
                                referenciaFirebase = referenciaFirebase
                                        .child("contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificadorContato);

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setNomeUsuario(usuarioContato.getNomeUsuario());
                                contato.setEmailUsuario(usuarioContato.getEmailUsuario());

                                referenciaFirebase.setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


}

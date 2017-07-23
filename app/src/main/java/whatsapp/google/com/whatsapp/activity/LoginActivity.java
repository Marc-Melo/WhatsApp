package whatsapp.google.com.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.AllStatic;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Permissao;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private TextView novoCadastro;
    private TextView txtEmail;
    private TextView txtSenha;
    private Button botaoLogar;
    private Usuario usuario;
    private String email, senha;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseReference;
    private ValueEventListener valueEventListener;

    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validarPermissoes(1, this, AllStatic.PERMISSOES_NECESSARIAS);

        verificarUsuarioLogado();

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        novoCadastro = (TextView)findViewById(R.id.txt_cadastre_aqui_id);
        novoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(getApplicationContext(), CadastroActivity.class);
                //Intent telaCadastro = new Intent(getApplicationContext(), NewUserStep1Activity.class);
                startActivity(telaCadastro);
            }
        });

        txtEmail = (TextView)findViewById(R.id.txt_email_id);
        txtSenha = (TextView)findViewById(R.id.txt_password_id);
        botaoLogar  = (Button)findViewById(R.id.btn_logar_id);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarProcessoCadastro();
            }
        });
    }

    private void inicializar(){
        email = txtEmail.getText().toString().trim();
        senha = txtSenha.getText().toString().trim();
    }

    private boolean validarCampos(){

        boolean retorno = true;

        if(email.isEmpty()  || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Por favor, insira um email válido.");
            retorno = false;
        }

        if(senha.isEmpty()){
            txtSenha.setError("Por favor, insira uma senha válida.");
            retorno = false;
        }

        return retorno;
    }

    public void realizarProcessoCadastro(){

        inicializar();
        if(!validarCampos()){
            mensagemFalhaLogin();
        }
        else{
            processarCadastro();
        }
    }

    private void mensagemFalhaLogin(){
        Toast.makeText(this, "Falha no Login! Favor corrigir os campos.", Toast.LENGTH_LONG).show();
    }

    private void processarCadastro(){
        usuario = new Usuario();
        usuario.setEmailUsuario(txtEmail.getText().toString());
        usuario.setSenhaUsuario(txtSenha.getText().toString());

        firebaseAuth.signInWithEmailAndPassword(usuario.getEmailUsuario(), usuario.getSenhaUsuario())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmailUsuario());

                            firebaseReference = FirebaseConnection.getFirebaseReference()
                                    .child("usuarios")
                                    .child(identificadorUsuarioLogado);

                            firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    usuario = dataSnapshot.getValue(Usuario.class);

                                    salvarPreferencias();
                                    abrirTelaPrincipal();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{

                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e){
                                txtEmail.setError("Login não cadastrado. Favor inserir um login válido.");
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                txtSenha.setError("Senha utilizada não confere.");
                            }
                            catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "Falha ao logar usuário.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void verificarUsuarioLogado(){
        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        Preferencias preferencias = new Preferencias(LoginActivity.this);
        Boolean cadastroFinalizado = preferencias.getCadastroFinalizado();

        if (firebaseAuth.getCurrentUser() != null && cadastroFinalizado){
            abrirTelaPrincipal();
        }
    }

    private void salvarPreferencias(){
        Preferencias preferencias = new Preferencias(getApplicationContext());
        preferencias.salvarDados(identificadorUsuarioLogado, usuario.getNomeUsuario(), true);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){

        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        for(int resultado: grantResult){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertarPermissao();
            }
        }
    }

    public void alertarPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package whatsapp.google.com.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class NewUserStep1Activity extends AppCompatActivity {

    private TextView emailUsuario;
    private TextView senhaUsuario;
    private Button btnProsseguir;
    private Usuario usuario;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_step1);

        VerificarUsuarioLogado();

        emailUsuario = (TextView)findViewById(R.id.step1_txt_email_id);
        senhaUsuario = (TextView)findViewById(R.id.step1_txt_password_id);
        btnProsseguir = (Button)findViewById(R.id.btn_prosseguir_id);

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        btnProsseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prosseguir();
            }
        });
    }

    private void Prosseguir(){

        usuario = new Usuario();
        usuario.setEmailUsuario     (emailUsuario.getText().toString());
        usuario.setSenhaUsuario     (senhaUsuario.getText().toString());

        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmailUsuario(), usuario.getSenhaUsuario())
                .addOnCompleteListener(NewUserStep1Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CadastrarUsuario();
                            AbrirTelaPrincipal();

                        }else{
                            StringBuilder erroExcecao = new StringBuilder("Erro: ");

                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e){
                                erroExcecao.append("A senha precisa ser mais forte!");
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcecao.append("E-mail inserido é inválido. Digite um novo e-mail.");
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                erroExcecao.append("Esse e-mail já está cadastrado.");
                            }
                            catch (Exception e){
                                erroExcecao.append("Falha ao cadastrar usuário.");
                                e.printStackTrace();
                            }

                            Toast.makeText(NewUserStep1Activity.this, erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void CadastrarUsuario(){
        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmailUsuario());
        usuario.setId( identificadorUsuario );
        usuario.salvar();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        preferencias.salvarDados(identificadorUsuario, null, null);

        Toast.makeText(NewUserStep1Activity.this, "Sucesso ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), NewUserStep2Activity.class);
        startActivity(intent);
        finish();
    }

    private void VerificarUsuarioLogado(){
        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        Preferencias preferencias = new Preferencias(NewUserStep1Activity.this);
        Boolean cadastroFinalizado = preferencias.getCadastroFinalizado();

        if (firebaseAuth.getCurrentUser() != null && cadastroFinalizado){
            AbrirTelaPrincipal();
        }
    }

}

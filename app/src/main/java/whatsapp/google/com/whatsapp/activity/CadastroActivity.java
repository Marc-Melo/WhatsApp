package whatsapp.google.com.whatsapp.activity;

import android.Manifest;
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
import com.google.firebase.auth.FirebaseUser;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class CadastroActivity extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView emailUsuario;
    private TextView telefoneUsuario;
    private TextView senhaUsuario;
    private Usuario usuario;
    private Button botaoCadastrar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nomeUsuario     = (TextView)findViewById(R.id.txt_nome_cadastro_id);
        emailUsuario    = (TextView)findViewById(R.id.txt_email_cadastro_id);
        telefoneUsuario = (TextView)findViewById(R.id.txt_telefone_cadastro_id);
        senhaUsuario    = (TextView)findViewById(R.id.txt_password_usuario_id);
        botaoCadastrar  = (Button)findViewById(R.id.btn_cadastrar_id);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarProcessoCadastro();
            }
        });
    }

    public void realizarProcessoCadastro(){

        usuario = new Usuario();
        usuario.setNomeUsuario      (nomeUsuario.getText().toString());
        usuario.setEmailUsuario     (emailUsuario.getText().toString());
        usuario.setTelefoneUsuario  (telefoneUsuario.getText().toString());
        usuario.setSenhaUsuario     (senhaUsuario.getText().toString());

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmailUsuario(), usuario.getSenhaUsuario())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
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

                            Toast.makeText(CadastroActivity.this, erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void CadastrarUsuario(){
        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmailUsuario());
        usuario.setId( identificadorUsuario );
        usuario.salvar();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        preferencias.salvarDados(identificadorUsuario, usuario.getNomeUsuario());

        Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}

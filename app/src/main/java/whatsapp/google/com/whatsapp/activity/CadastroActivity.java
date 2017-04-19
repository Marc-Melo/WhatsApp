package whatsapp.google.com.whatsapp.activity;

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
import com.google.firebase.database.DatabaseReference;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView emailUsuario;
    private TextView telefoneUsuario;
    private TextView senhaUsuario;
    private Usuario usuario;
    private Button botaoCadastrar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nomeUsuario = (TextView)findViewById(R.id.txt_nome_cadastro_id);
        emailUsuario = (TextView)findViewById(R.id.txt_email_cadastro_id);
        telefoneUsuario = (TextView)findViewById(R.id.txt_telefone_cadastro_id);
        senhaUsuario = (TextView)findViewById(R.id.txt_password_usuario_id);
        botaoCadastrar = (Button)findViewById(R.id.btn_cadastrar_id);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarProcessoCadastro();
            }
        });
    }

    public void realizarProcessoCadastro(){

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(emailUsuario.getText().toString(), senhaUsuario.getText().toString())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            realizarCadastro();
                        }else{

                        }
                    }
                });
    }

    public void realizarCadastro(){
        databaseReference = FirebaseConnection.getFirebaseReference();

        usuario = new Usuario(
                nomeUsuario.getText().toString(),
                emailUsuario.getText().toString(),
                telefoneUsuario.getText().toString(),
                senhaUsuario.getText().toString());

        databaseReference.child("Usuario").setValue(usuario);
        Toast.makeText(this, "Usuário Cadastrado.", Toast.LENGTH_SHORT).show();

    }
}

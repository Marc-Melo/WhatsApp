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

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;

public class LoginActivity extends AppCompatActivity {

    private TextView novoCadastro;
    private TextView email;
    private TextView senha;
    private Button botaoLogar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        novoCadastro = (TextView)findViewById(R.id.txt_cadastre_aqui_id);
        novoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(telaCadastro);
            }
        });

        email = (TextView)findViewById(R.id.txt_email_id);
        senha = (TextView)findViewById(R.id.txt_password_id);
        botaoLogar = (Button)findViewById(R.id.btn_logar_id);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Usuário autenticado. Direcionar para tela principal.", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(firebaseAuth.getCurrentUser() != null){
                                        Toast.makeText(LoginActivity.this, "Usuário já autenticado. Direcionar pra tela principal", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                );
            }
        });

    }
}

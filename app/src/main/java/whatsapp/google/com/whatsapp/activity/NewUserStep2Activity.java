package whatsapp.google.com.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class NewUserStep2Activity extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView foneUsuario;
    private Button btnCadastrar;
    private Usuario usuario;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_step2);

        nomeUsuario = (TextView)findViewById(R.id.step2_txt_nome_id);
        foneUsuario = (TextView)findViewById(R.id.step2_txt_telefone_id);
        btnCadastrar = (Button)findViewById(R.id.step2_btn_cadastrar_usuario_id);

        firebaseAuth = FirebaseConnection.getFirebaseAuth();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCadastro();
                AbrirTelaPrincipal();
            }
        });
    }

    private void FinalizarCadastro(){
        Preferencias preferencias = new Preferencias(getApplicationContext());
        String identificadorUsuario = preferencias.getIdentificador();

        usuario = new Usuario();
        usuario.setNomeUsuario     (nomeUsuario.getText().toString());
        usuario.setTelefoneUsuario (foneUsuario.getText().toString());
        usuario.setEmailUsuario    (Base64Custom.decodificarBase64(identificadorUsuario));
        usuario.setId( identificadorUsuario );
        usuario.salvar();

        preferencias.salvarDados(identificadorUsuario, usuario.getNomeUsuario());

        Toast.makeText(NewUserStep2Activity.this, "Sucesso ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}

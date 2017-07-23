package whatsapp.google.com.whatsapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class CadastroActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView nomeUsuario;
    private TextView emailUsuario;
    private TextView telefoneUsuario;
    private TextView senhaUsuario;
    private Usuario usuario;
    private Button botaoCadastrar;
    private String nome, telefone, email, senha;

    private FirebaseAuth firebaseAuth;

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        circleImageView = (CircleImageView)findViewById(R.id.photo_user_id);
        nomeUsuario = (TextView) findViewById(R.id.nome_user_id);
        emailUsuario = (TextView) findViewById(R.id.email_user_id);
        telefoneUsuario = (TextView) findViewById(R.id.telefone_user_id);
        senhaUsuario = (TextView) findViewById(R.id.senha_user_id);
        botaoCadastrar = (Button) findViewById(R.id.botao_cadastrar_usuario_id);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefoneUsuario, smf);
        telefoneUsuario.addTextChangedListener(maskTelefone);

        firebaseAuth = FirebaseConnection.getFirebaseAuth();
        firebaseStorage = firebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://whatsapp-6d8dc.appspot.com");
        progressDialog = new ProgressDialog(this);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarProcessoCadastro();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                circleImageView.setImageBitmap(bm);

            }catch (FileNotFoundException ex){
                ex.printStackTrace();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void inicializar(){

        nome = nomeUsuario.getText().toString().trim();
        email = emailUsuario.getText().toString().trim();
        telefone = telefoneUsuario.getText().toString().trim();
        senha = senhaUsuario.getText().toString().trim();
    }

    public boolean validarCampos(){
        boolean retorno = true;

        if(nome.isEmpty() || nome.length() > 40){
            nomeUsuario.setError("Por favor, entre com um nome válido.");
            retorno = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUsuario.setError("Por favor, entre com um e-mail válido.");
            retorno = false;
        }

        if(senha.isEmpty()){
            senhaUsuario.setError("Por favor, entre com uma senha válida.");
            retorno = false;
        }

        if(telefone.isEmpty()){
            telefoneUsuario.setError("Por favor, entre com um telefone válido.");
            retorno = false;
        }

        return retorno;
    }


    public void realizarProcessoCadastro(){

        inicializar();
        if(!validarCampos()){
            mensagemFalhaCadastro();
        }
        else{
            processarCadastro();
        }
    }

    private void mensagemFalhaCadastro(){
        Toast.makeText(this, "Falha no Cadastro! Favor corrigir os campos.", Toast.LENGTH_LONG).show();
    }

    private void processarCadastro(){
        usuario = new Usuario();
        usuario.setNomeUsuario      (nome);
        usuario.setEmailUsuario     (email);
        usuario.setTelefoneUsuario  (telefone);
        usuario.setSenhaUsuario     (senha);

        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmailUsuario(), usuario.getSenhaUsuario())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CadastrarUsuario();
                            FazerUploadFoto();

                        }else{
                            StringBuilder erroExcecao = new StringBuilder("Erro: ");

                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e){
                                senhaUsuario.setError("Sua senha deve conter pelo menos 6 caracteres.");
                                mensagemFalhaCadastro();
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                emailUsuario.setError("Esse email já está cadastrado.");
                                mensagemFalhaCadastro();
                            }
                            catch (Exception e){
                                Toast.makeText(CadastroActivity.this, "Falha ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void CadastrarUsuario(){
        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmailUsuario());
        usuario.setId( identificadorUsuario );
        usuario.salvar();

        Preferencias preferencias = new Preferencias(getApplicationContext());
        preferencias.salvarDados(identificadorUsuario, usuario.getNomeUsuario(), true);
    }

    private void FazerUploadFoto(){

        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        circleImageView.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        Preferencias preferencias = new Preferencias(CadastroActivity.this);
        StorageReference filePath = storageReference.child("images/"+preferencias.getIdentificador());

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        UploadTask uploadTask = filePath.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CadastroActivity.this, "Upload Failed.", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(CadastroActivity.this, "Upload Done.", Toast.LENGTH_LONG).show();

                usuario.setUrlPhotoUser(taskSnapshot.getDownloadUrl().toString());
                usuario.salvar();

                AbrirTelaPrincipal();
            }
        });
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}

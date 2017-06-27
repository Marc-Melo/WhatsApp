package whatsapp.google.com.whatsapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Usuario;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class NewUserStep2Activity extends AppCompatActivity {

    private TextView nomeUsuario;
    private TextView foneUsuario;
    private Button btnCadastrar;
    private CircleImageView circleImageView;
    private Usuario usuario;

    private FirebaseAuth firebaseAuth;
    private static final int GALLERY_INTENT = 2;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;
    private String identificadorUsuario;
    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_step2);

        preferencias = new Preferencias(getApplicationContext());
        identificadorUsuario = preferencias.getIdentificador();

        nomeUsuario = (TextView)findViewById(R.id.step2_txt_nome_id);
        foneUsuario = (TextView)findViewById(R.id.step2_txt_telefone_id);
        btnCadastrar = (Button)findViewById(R.id.step2_btn_cadastrar_usuario_id);
        circleImageView = (CircleImageView)findViewById(R.id.civ_round);

        firebaseAuth = FirebaseConnection.getFirebaseAuth();
        firebaseStorage = firebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://whatsapp-6d8dc.appspot.com");
        progressDialog = new ProgressDialog(this);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCadastro();
                FazerUploadFoto();
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

    private void FinalizarCadastro(){

        usuario = new Usuario();
        usuario.setNomeUsuario     (nomeUsuario.getText().toString());
        usuario.setTelefoneUsuario (foneUsuario.getText().toString());
        usuario.setEmailUsuario    (Base64Custom.decodificarBase64(identificadorUsuario));
        usuario.setId( identificadorUsuario );
        usuario.salvar();

        preferencias.salvarDados(identificadorUsuario, usuario.getNomeUsuario(), true);

        Toast.makeText(NewUserStep2Activity.this, "Sucesso ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
    }

    private void FazerUploadFoto(){

        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        circleImageView.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        Preferencias preferencias = new Preferencias(NewUserStep2Activity.this);
        StorageReference filePath = storageReference.child("images/"+preferencias.getIdentificador());

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        UploadTask uploadTask = filePath.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NewUserStep2Activity.this, "Upload Failed.", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(NewUserStep2Activity.this, "Upload Done.", Toast.LENGTH_LONG).show();

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

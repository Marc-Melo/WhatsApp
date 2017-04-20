package whatsapp.google.com.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        auth = FirebaseConnection.getFirebaseAuth();
        auth.createUserWithEmailAndPassword("teste2@teste.com", "abc123456").addOnCompleteListener(
                MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Autenticado", Toast.LENGTH_SHORT).show();
                        }else {
                            if(auth.getCurrentUser() != null){
                                Toast.makeText(MainActivity.this, "Já Autenticado", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "Não Autenticado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
        */
    }
}

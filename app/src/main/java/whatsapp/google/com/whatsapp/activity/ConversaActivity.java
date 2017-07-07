package whatsapp.google.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.adapter.MensagemAdapter;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Conversa;
import whatsapp.google.com.whatsapp.model.Mensagem;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Bundle bundle;
    private EditText textoMensagem;
    private ImageButton btnEnviarMensagem;

    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    private DatabaseReference firebaseReferencia;

    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ListView listView;

    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar)findViewById(R.id.toobar_conversas);
        textoMensagem = (EditText)findViewById(R.id.edit_mensagem_conversa);
        btnEnviarMensagem = (ImageButton)findViewById(R.id.img_send_mensagem_conversa);
        listView = (ListView)findViewById(R.id.lv_conversas);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNomeUsuario();

        bundle = getIntent().getExtras();

        if(bundle != null){
            nomeUsuarioDestinatario = bundle.getString("nome");
            String emailUsuarioDestinatario = bundle.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailUsuarioDestinatario);
        }

        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);

        listView.setAdapter(adapter);

        firebaseReferencia = FirebaseConnection.getFirebaseReference()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Mensagem mensagem = snapshot.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseReferencia.addValueEventListener(valueEventListenerMensagem);

        btnEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String texto = textoMensagem.getText().toString();
                if(!texto.isEmpty()){

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(texto);

                    Boolean retornoMensagemRemetente = salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                    if(!retornoMensagemRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem. Tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);
                        if(!retornoMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem. Tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(texto);
                    Boolean retornoConversaRemetente = salvarConversas(idUsuarioRemetente, idUsuarioDestinatario, conversa);
                    if(!retornoConversaRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar conversa. Tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(texto);
                        Boolean retornoConversaDestinatario = salvarConversas(idUsuarioDestinatario, idUsuarioRemetente, conversa);
                        if(!retornoConversaDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar conversa. Tente novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    textoMensagem.setText("");
                }
            }
        });
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try {
            firebaseReferencia = FirebaseConnection.getFirebaseReference().child("mensagens");

            firebaseReferencia.child( idRemetente )
                    .child( idDestinatario )
                    .push()
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarConversas(String idRemetente, String idDestinatario, Conversa conversa){
        try {
            firebaseReferencia = FirebaseConnection.getFirebaseReference().child("conversas");

            firebaseReferencia.child( idRemetente )
                    .child( idDestinatario )
                    .setValue(conversa);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseReferencia.removeEventListener(valueEventListenerMensagem);
    }
}

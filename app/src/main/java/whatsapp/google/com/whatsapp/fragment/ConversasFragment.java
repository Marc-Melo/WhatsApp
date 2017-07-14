package whatsapp.google.com.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.activity.ConversaActivity;
import whatsapp.google.com.whatsapp.adapter.ContatoAdaptar;
import whatsapp.google.com.whatsapp.adapter.ConversaAdapter;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Contato;
import whatsapp.google.com.whatsapp.model.Conversa;
import whatsapp.google.com.whatsapp.util.Base64Custom;
import whatsapp.google.com.whatsapp.util.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        //databaseReference.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();

        //databaseReference.removeEventListener(valueEventListenerConversas);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        conversas = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        listView = (ListView)view.findViewById(R.id.lv_conversas);

        adapter = new ConversaAdapter(getActivity(), conversas);

        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = FirebaseConnection.getFirebaseReference().child("conversas").child(identificadorUsuarioLogado);

        databaseReference.addValueEventListener(valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Conversa conversa = conversas.get(position);

                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decodificarBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        return view;

    }

}

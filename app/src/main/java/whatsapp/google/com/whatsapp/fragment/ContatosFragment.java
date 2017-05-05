package whatsapp.google.com.whatsapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Contato;
import whatsapp.google.com.whatsapp.util.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {


    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> contatos;
    private DatabaseReference databaseReference;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView)view.findViewById(R.id.lv_contatos);

        adapter = new ArrayAdapter(
          getActivity(), R.layout.lista_contato, contatos
        );

        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = FirebaseConnection.getFirebaseReference().child("contatos").child(identificadorUsuarioLogado);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contatos.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato.getNomeUsuario());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

}

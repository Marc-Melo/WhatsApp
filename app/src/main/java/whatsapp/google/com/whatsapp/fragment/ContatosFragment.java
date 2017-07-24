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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.activity.ConversaActivity;
import whatsapp.google.com.whatsapp.adapter.ContatoAdaptar;
import whatsapp.google.com.whatsapp.adapter.ContatoLinkedAdapter;
import whatsapp.google.com.whatsapp.config.FirebaseConnection;
import whatsapp.google.com.whatsapp.model.Contato;
import whatsapp.google.com.whatsapp.util.Preferencias;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {


    private ListView listView, listView2;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerContatos;


    private ArrayList<LinkedHashMap<String, Contato>> arrayListHash;
    private LinkedHashMap<String, Contato> linkedHashMap;
    private ContatoLinkedAdapter contatoLinkedAdapter;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        //databaseReference.addValueEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();

        //databaseReference.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*

         */
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listView = (ListView)view.findViewById(R.id.lv_contatos);

        adapter = new ContatoAdaptar(getActivity(), contatos);

        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = FirebaseConnection.getFirebaseReference().child("contatos").child(identificadorUsuarioLogado);


        /*

        //Trying LinkedHashMap
        arrayListHash = new ArrayList<LinkedHashMap<String, Contato>>();
        linkedHashMap = new LinkedHashMap<String, Contato>();

        // Inflate the layout for this fragment
        View view2 = inflater.inflate(R.layout.fragment_contatos, container, false);
        listView2 = (ListView)view2.findViewById(R.id.lv_contatos);
        contatoLinkedAdapter = new ContatoLinkedAdapter(getActivity(), arrayListHash);
        listView2.setAdapter(contatoLinkedAdapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        databaseReference = FirebaseConnection.getFirebaseReference().child("contatos").child(identificadorUsuarioLogado);


        //Fim LinkedHashMap
        */

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /*

                linkedHashMap.put(dataSnapshot.getValue(Contato.class).getIdentificadorUsuario(),
                        dataSnapshot.getValue(Contato.class));
                //arrayListHash.add(linkedHashMap);
                contatoLinkedAdapter.notifyDataSetChanged();
                */

                /*
                Converter o Array num LinkedHashMap e trabalhar nele..
                Depois converter novamente em ArrayList

                Tentar isso -> List<String> valueList = new ArrayList<String>(map.values());
                 */
                contatos.add(dataSnapshot.getValue(Contato.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*
                contatos.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }
                adapter.notifyDataSetChanged();
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Contato contato = contatos.get(position);

                intent.putExtra("nome", contato.getNomeUsuario());
                intent.putExtra("email", contato.getEmailUsuario());

                startActivity(intent);
            }
        });


        return view;
        //return view2;
    }

}

package whatsapp.google.com.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.model.Contato;
import whatsapp.google.com.whatsapp.model.Conversa;

/**
 * Created by marcilio.s.melo on 10/05/2017.
 */

public class ConversaAdapter extends ArrayAdapter<Conversa>{

    private ArrayList<Conversa> conversas;
    private Context context;


    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);

        this.conversas = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(conversas != null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lista_conversas, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_titulo);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.tv_subtitulo);

            Conversa conversa = conversas.get(position);
            nomeContato.setText(conversa.getNome());
            ultimaMensagem.setText(conversa.getMensagem());
        }

        return view;
    }
}

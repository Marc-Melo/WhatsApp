package whatsapp.google.com.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.model.Contato;

/**
 * Created by marci on 23/07/2017.
 */

public class ContatoLinkedAdapter extends BaseAdapter {

    private ArrayList<LinkedHashMap<String, Contato>> _contatos;
    private Context _context;

    public ContatoLinkedAdapter(Context context, ArrayList<LinkedHashMap<String, Contato>> linked){

        this._contatos = linked;
        this._context = context;

    }

    @Override
    public int getCount() {

        return _contatos.size();
    }

    @Override
    public LinkedHashMap<String, Contato> getItem(int position) {

        return _contatos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(_contatos != null){

            LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.lista_contato, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome_contato);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email_contato);
            final ImageView fotoContato = (ImageView) view.findViewById(R.id.civ_foto_contato);

            for(LinkedHashMap<String, Contato> contatoHash : _contatos){
                String key = contatoHash.toString();

            }
        }

        return null;
    }
}

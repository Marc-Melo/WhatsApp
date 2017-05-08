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

public class ContatoAdaptar extends ArrayAdapter<Contato> {

    private ArrayList<Contato> _contatos;
    private Context _context;

    public ContatoAdaptar(Context context, ArrayList<Contato> objects) {
        super(context, 0, objects);

        this._contatos = objects;
        this._context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(_contatos != null){

            LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.lista_contato, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome_contato);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email_contato);

            Contato contato = _contatos.get(position);
            nomeContato.setText(contato.getNomeUsuario());
            emailContato.setText(contato.getEmailUsuario());

        }

        return view;
    }
}

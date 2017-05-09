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
import whatsapp.google.com.whatsapp.model.Mensagem;
import whatsapp.google.com.whatsapp.util.Preferencias;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);

        this.context = c;
        this.mensagens = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mensagens != null){

            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Mensagem mensagem = mensagens.get(position);

            if(idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = layoutInflater.inflate(R.layout.layout_conversa_enviada, parent, false);
            }else{
                view = layoutInflater.inflate(R.layout.layout_conversa_recebida, parent, false);
            }

            TextView textoMensagem = (TextView)view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());

        }

        return view;
    }
}

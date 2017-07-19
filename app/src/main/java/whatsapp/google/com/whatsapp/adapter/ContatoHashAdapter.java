package whatsapp.google.com.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import whatsapp.google.com.whatsapp.R;
import whatsapp.google.com.whatsapp.model.Contato;

/**
 * Created by marci on 18/07/2017.
 */

public class ContatoHashAdapter extends BaseAdapter {

    private final ArrayList mData;
    private Context _context;

    public ContatoHashAdapter(Map<String, Contato> map, Context context) {
        this.mData = new ArrayList();
        this.mData.addAll(map.entrySet());
        this._context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Contato> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mData != null){

            LayoutInflater layoutInflater = (LayoutInflater)_context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.lista_contato, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome_contato);
            TextView emailContato = (TextView) view.findViewById(R.id.tv_email_contato);
            final ImageView fotoContato = (ImageView) view.findViewById(R.id.civ_foto_contato);

            Map.Entry<String, Contato> contatosHash = getItem(position);


            /*
            Contato contato = _contatos.get(position);
            nomeContato.setText(contato.getNomeUsuario());
            emailContato.setText(contato.getEmailUsuario());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(contato.getUrlPhoto());

            final long ONE_MEGABYTE = 1024 * 1024;

            //download file as a byte array
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    fotoContato.setImageBitmap(bitmap);
                }
            });

            */

        }

        return view;
    }
}

package whatsapp.google.com.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
            final ImageView fotoContato = (ImageView) view.findViewById(R.id.civ_foto_contato);

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

        }

        return view;
    }
}

package whatsapp.google.com.whatsapp.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marci on 06/07/2017.
 */

public class Permissao {

    public static boolean validarPermissoes(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23) {

            List<String> listaPermissao = new ArrayList<String>();

            for (String permissao : permissoes) {
                Boolean permitido = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if (!permitido) listaPermissao.add(permissao);
            }

            if (listaPermissao.isEmpty()) return true;

            String[] necessitaPermissao = new String[listaPermissao.size()];
            listaPermissao.toArray(necessitaPermissao);

            ActivityCompat.requestPermissions(activity, necessitaPermissao, requestCode);
        }

        return true;
    }
}

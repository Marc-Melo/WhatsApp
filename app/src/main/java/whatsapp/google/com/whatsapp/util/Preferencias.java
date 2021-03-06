package whatsapp.google.com.whatsapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marcilio.s.melo on 04/05/2017.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    private final String CHAVE_CADASTRO_COMPLETO = "cadastroFinalizado";

    public Preferencias(Context contextoParametro){
        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario, String nomeUsuario, Boolean cadastroFinalizado){
        if(identificadorUsuario != null)
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);

        if(nomeUsuario != null)
        editor.putString(CHAVE_NOME, nomeUsuario);

        if(cadastroFinalizado != null)
        editor.putBoolean(CHAVE_CADASTRO_COMPLETO, cadastroFinalizado);

        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNomeUsuario(){
        return preferences.getString(CHAVE_NOME, null);
    }

    public Boolean getCadastroFinalizado(){ return preferences.getBoolean (CHAVE_CADASTRO_COMPLETO, false); }

}

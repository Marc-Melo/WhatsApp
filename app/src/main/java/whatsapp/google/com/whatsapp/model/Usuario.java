package whatsapp.google.com.whatsapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import whatsapp.google.com.whatsapp.config.FirebaseConnection;

/**
 * Created by marcilio.s.melo on 19/04/2017.
 */

public class Usuario {

    private String idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
    private String senhaUsuario;
    private String urlPhotoUser;

    public Usuario(){
    }

    public void salvar(){

        DatabaseReference referenciaFirebase = FirebaseConnection.getFirebaseReference();
        referenciaFirebase.child("usuarios").child( getId() ).setValue( this );
    }

    @Exclude
    public String getId() { return idUsuario; }

    public void setId(String id) { this.idUsuario = id; }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public void setTelefoneUsuario(String telefoneUsuario) { this.telefoneUsuario = telefoneUsuario; }

    @Exclude
    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) { this.urlPhotoUser = urlPhotoUser; }


}

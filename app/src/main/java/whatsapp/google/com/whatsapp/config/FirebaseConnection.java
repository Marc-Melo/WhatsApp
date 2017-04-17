package whatsapp.google.com.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by marcilio.s.melo on 17/04/2017.
 */

public final class FirebaseConnection {

    private static FirebaseAuth auth;
    private static DatabaseReference referenciaFirebase;

    public static FirebaseAuth getFirebaseAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static DatabaseReference getFirebaseReference(){
        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

}

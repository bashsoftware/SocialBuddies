package bash.socialbuddies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.fragments.FragmentLogin;
import bash.socialbuddies.fragments.FragmentRegister;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class LoginActivity extends AppCompatActivity {

    private FrameLayout frmContainer;
    private DatabaseReference database;

    private int fragmentActual = 0;

    private Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControls();
        displayScreen(R.layout.login_activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());

        fragmentActual = R.layout.login_activity_login;

        FirebaseAuth.getInstance().signOut();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //FirebaseAuth.getInstance().signOut();
    }

    private void initControls() {
        database = FirebaseDatabase.getInstance().getReference();
        frmContainer = findViewById(R.id.container_login);
    }

    public void displayScreen(int id) {
        fragment = null;
        fragmentActual = id;
        switch (id) {
            case R.layout.login_activity_login:
                fragment = new FragmentLogin();
                break;
            case R.layout.login_activity_register:
                fragment = new FragmentRegister();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(frmContainer.getId(), fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentActual != R.layout.login_activity_login) {
            displayScreen(R.layout.login_activity_login);
        }
    }

    public void login(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void register(final BeanUsuario usuario, String password) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario.getUsu_correo(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    usuario.setUsu_id(task.getResult().getUser().getUid());

                    database.child(FirebaseReference.USUARIOS).child(usuario.getUsu_id()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "USUARIOS registrado con éxito", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Error al iniciar sesión " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void handleFacebookAccessToken(final AccessToken token) {
        Singleton.getInstancia().setBeanUsuario(new BeanUsuario());
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            BeanUsuario usuario = Singleton.getInstancia().getBeanUsuario();
                            //usuario.setUsu_nombre(response.getJSONObject().getString("first_name"));
                            //usuario.setUsu_apellido(response.getJSONObject().getString("last_name"));
                            usuario.setUsu_correo(response.getJSONObject().getString("email"));
                            usuario.setUsu_edad(18);
                        } catch (Exception e) {

                        }

                    }
                });


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            BeanUsuario usuario = Singleton.getInstancia().getBeanUsuario();
                            usuario.setUsu_id(task.getResult().getUser().getUid());
                            database.child(FirebaseReference.USUARIOS).child(usuario.getUsu_id()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "USUARIOS registrado con éxito", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((FragmentLogin) fragment).onActivityResult(requestCode, resultCode, data);

    }
}

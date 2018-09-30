package bash.socialbuddies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.fragments.FragmentLogin;
import bash.socialbuddies.fragments.FragmentRegister;
import bash.socialbuddies.utilities.FirebaseReference;

public class LoginActivity extends AppCompatActivity {

    private FrameLayout frmContainer;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControls();
        displayScreen(R.layout.login_activity_login);

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
        Fragment fragment = null;
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

}

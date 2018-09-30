package bash.socialbuddies.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.MainActivity;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.fragments.FragmentLogin;
import bash.socialbuddies.fragments.FragmentRegister;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class LoginActivity extends AppCompatActivity {

    private FrameLayout frmContainer;
    private FirebaseAuth auth;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControls();
        displayScreen(R.layout.login_activity_login);
    }

    private void initControls(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        frmContainer = findViewById(R.id.container_login);
    }



    public void displayScreen(int id){
        Fragment fragment = null;
        switch (id){
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

    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void register(final BeanUsuario usuario, String password){
        auth.createUserWithEmailAndPassword(usuario.getUsu_correo(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            usuario.setUsu_id(task.getResult().getUser().getUid());
                            Singleton.getInstancia().setBeanUsuario(usuario);

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/" + FirebaseReference.USUARIO  + "/" + task, usuario.toMap());

                            database.updateChildren(childUpdates);
                            displayScreen(R.layout.login_activity_login);
                        }else{
                            //Log.d("dfd", );
                            Toast.makeText(getApplicationContext(),"Error al iniciar sesión " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

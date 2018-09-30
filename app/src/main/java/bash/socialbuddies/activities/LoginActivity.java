package bash.socialbuddies.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView txvRegistro;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initControls();
        initEvents();
    }

    private void initControls(){
        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        edtPass = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        txvRegistro = findViewById(R.id.txvRegistro);
    }

    private void initEvents(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
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
        });

        txvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

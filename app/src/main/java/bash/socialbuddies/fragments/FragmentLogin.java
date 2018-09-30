package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.LoginActivity;

public class FragmentLogin extends Fragment {
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView txvRegistro;
    private View view;
    private LoginButton btnFacebook;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_activity_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls();
        initEvents((LoginActivity)(getActivity()));
        getActivity().setTitle("Registro");
    }

    private void initControls(){
        btnLogin = view.findViewById(R.id.btnLogin);
        edtPass = view.findViewById(R.id.edtPass);
        edtEmail = view.findViewById(R.id.edtEmail);
        txvRegistro = view.findViewById(R.id.txvRegistro);
        btnFacebook = view.findViewById(R.id.login_button);
    }

    private void initEvents(final LoginActivity activity){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edtEmail.getText()) || TextUtils.isEmpty(edtPass.getText())){
                    Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else{
                    activity.login(edtEmail.getText().toString(), edtPass.getText().toString());
                }

            }
        });

        txvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayScreen(R.layout.login_activity_register);
            }
        });

        CallbackManager callbackManager = CallbackManager.Factory.create();

        btnFacebook.setReadPermissions("email");
        // If using in a fragment
        btnFacebook.setFragment(this);
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                activity.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(), "Error en el inicio de Facebook: " + exception.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

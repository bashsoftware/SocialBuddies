package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.LoginActivity;

public class FragmentLogin extends Fragment {
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private TextView txvRegistro;
    private View view;



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
    }

    private void initEvents(final LoginActivity activity){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.login(edtEmail.getText().toString(), edtPass.getText().toString());
            }
        });

        txvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayScreen(R.layout.login_activity_register);
            }
        });
    }
}

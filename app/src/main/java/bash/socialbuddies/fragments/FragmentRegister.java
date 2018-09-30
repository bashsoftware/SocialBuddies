package bash.socialbuddies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.LoginActivity;
import bash.socialbuddies.beans.BeanUsuario;

public class FragmentRegister extends Fragment {
    private View view;

    EditText edtEmailRegister;
    EditText edtPassRegister;
    EditText edtNombreRegister;
    EditText edtApellidoRegister;
    EditText edtEdadRegister;
    Button btnRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_activity_register, container, false);
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
        edtEmailRegister = view.findViewById(R.id.edtEmailRegister);
        edtPassRegister = view.findViewById(R.id.edtPassRegister);
        edtNombreRegister = view.findViewById(R.id.edtNombreRegister);
        edtApellidoRegister = view.findViewById(R.id.edtApellidoRegister);
        edtEdadRegister = view.findViewById(R.id.edtEdadRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
    }

    private void initEvents(final LoginActivity activity){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeanUsuario usuario = new BeanUsuario();
                usuario.setUsu_correo(edtEmailRegister.getText().toString());
                usuario.setUsu_nombre(edtNombreRegister.getText().toString());
                usuario.setUsu_apellido(edtApellidoRegister.getText().toString());
                usuario.setUsu_edad(Integer.valueOf(edtEdadRegister.getText().toString()));
                activity.register(usuario, edtPassRegister.getText().toString());
            }
        });
    }
}

package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import android.net.Uri;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.LoginActivity;
import bash.socialbuddies.beans.BeanUsuario;

import static android.app.Activity.RESULT_OK;

public class FragmentRegister extends Fragment {
    private View view;

    EditText edtEmailRegister;
    EditText edtPassRegister;
    EditText edtNombreRegister;
    EditText edtApellidoRegister;
    EditText edtEdadRegister;
    Button btnRegister;
    Button btnFacebook;
    ImageButton imgPerfil;

    Uri uri = null;

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
        btnFacebook = view.findViewById(R.id.login_button);
        imgPerfil = view.findViewById(R.id.imgPerfil);
    }

    private void initEvents(final LoginActivity activity){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeanUsuario usuario = new BeanUsuario();
                if(TextUtils.isEmpty(edtEmailRegister.getText()) ||
                    TextUtils.isEmpty(edtNombreRegister.getText()) ||
                    TextUtils.isEmpty(edtApellidoRegister.getText())||
                    TextUtils.isEmpty(edtEdadRegister.getText()) ||
                    TextUtils.isEmpty(edtPassRegister.getText()) ||
                        uri == null){
                    Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else{
                    usuario.setUsu_correo(edtEmailRegister.getText().toString());
                    usuario.setUsu_nombre(edtNombreRegister.getText().toString());
                    usuario.setUsu_apellido(edtApellidoRegister.getText().toString());
                    usuario.setUsu_edad(Integer.valueOf(edtEdadRegister.getText().toString()));
                    activity.register(usuario, edtPassRegister.getText().toString(), uri);
                }

            }
        });

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });
    }

    public void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            imgPerfil.setImageURI(uri);
        }
    }
}

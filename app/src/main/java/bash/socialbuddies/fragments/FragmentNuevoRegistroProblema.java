package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.MapsActivityRegistro;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.beans.BeanUbicacion;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class FragmentNuevoRegistroProblema extends Fragment {
    public static BeanUbicacion latLng = null;
    private EditText titulo, descripcion;
    private Button coordenadas, enviar;
    private ArrayList<String> imgs;
    private Spinner spinner;
    private ArrayList<String> sp;
    public static ArrayList<bash.socialbuddies.beans.BeanUbicacion> puntos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_problema, container, false);
        super.onCreate(savedInstanceState);

        spinner = (Spinner) view.findViewById(R.id.fragment_registro_problema_spinner);
        titulo = (EditText) view.findViewById(R.id.fragment_registro_problema_titulo);
        descripcion = (EditText) view.findViewById(R.id.fragment_registro_problema_descripcion);
        coordenadas = (Button) view.findViewById(R.id.fragment_registro_problema_coordenadas);
        enviar = (Button) view.findViewById(R.id.fragment_registro_problema_enviar);

        sp = new ArrayList<String>();
        sp.add("Choques");
        sp.add("Socavon");
        sp.add("Inundacion");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                sp);
        spinner.setAdapter(spinnerArrayAdapter);
        enviar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validar();
                    }
                });
        coordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivityRegistro.class);
                i.putExtra("basura", "basura");
                startActivity(i);
            }
        });
        return view;
    }

    void validar() {
        finalizar();
    }

    void finalizar() {
        if (latLng != null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            BeanUsuario usuario = Singleton.getInstancia().getBeanUsuario();


            BeanMotivo motivo = new BeanMotivo();
            motivo.setMot_tipo(spinner.getSelectedItem().toString());
            motivo.setMot_titulo(titulo.getText().toString());

            BeanIncidente incidente = new BeanIncidente();
            incidente.setInc_titulo(titulo.getText().toString());
            incidente.setInc_descripcion(descripcion.getText().toString());
            incidente.setInc_imgs(imgs);
            incidente.setUbicacion(latLng);
            incidente.setUsuario(usuario);
            incidente.setMotivo(motivo);
            incidente.setPuntos(puntos);

            Date currentTime = Calendar.getInstance().getTime();
            incidente.setInc_fecha(currentTime.getTime());
            db.child(FirebaseReference.INCIDENTES).child(incidente.getMotivo().getMot_tipo()).push().setValue(incidente);

            latLng = null;

        }
    }
}

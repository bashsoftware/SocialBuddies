package bash.socialbuddies.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import bash.socialbuddies.MapsActivity;
import bash.socialbuddies.R;
import bash.socialbuddies.utilities.FirebaseReference;

public class fragment_nuevo_registro_problema extends Fragment {
   private TextView titulo,descripcion;
   private Button coordenadas, enviar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_problema,container);
        super.onCreate(savedInstanceState);

        titulo = (TextView) view.findViewById(R.id.fragment_registro_problema_titulo);
        descripcion = (TextView) view.findViewById(R.id.fragment_registro_problema_descripcion);
        coordenadas = (Button) view.findViewById(R.id.fragment_registro_problema_coordenadas);
        enviar = (Button) view.findViewById(R.id.fragment_registro_problema_enviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
        coordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MapsActivity.class);
                startActivity(i);
            }
        });
        return  view;
    }
    void validar(){
        finalizar();
    }
    void finalizar(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(FirebaseReference.INCIDENTE).push();
    }
}

package bash.socialbuddies.fragments;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.MapsActivityRegistro;
import bash.socialbuddies.adapters.AdaptadorImagenesHorizontal;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.beans.BeanUbicacion;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

import static android.app.Activity.RESULT_OK;

public class FragmentNuevoRegistroProblema extends Fragment {
    public static BeanUbicacion latLng = null;
    private EditText titulo, descripcion;
    private Button coordenadas, enviar,imgs;
    private ArrayList<String> imgs2;
    private Spinner spinner;
    private ArrayList<String> sp;
    private ArrayList<Uri>  uris;
    private RecyclerView recycler;
    private ArrayList<String>urls;
    private AdaptadorImagenesHorizontal adapter;

    public static ArrayList<bash.socialbuddies.beans.BeanUbicacion> puntos;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_problema, container, false);
        super.onCreate(savedInstanceState);
        uris = new ArrayList();

        spinner = (Spinner) view.findViewById(R.id.fragment_registro_problema_spinner);
        titulo = (EditText) view.findViewById(R.id.fragment_registro_problema_titulo);
        descripcion = (EditText) view.findViewById(R.id.fragment_registro_problema_descripcion);
        coordenadas = (Button) view.findViewById(R.id.fragment_registro_problema_coordenadas);
        enviar = (Button) view.findViewById(R.id.fragment_registro_problema_enviar);
        imgs = (Button) view.findViewById(R.id.fragment_registro_problema_imgs);
        recycler = (RecyclerView) view.findViewById(R.id.fragment_registro_problema_imagnes);
        imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });

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

               subirAchivos();     }
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
            incidente.setInc_imgs(urls);
            incidente.setUbicacion(latLng);
            incidente.setUsuario(usuario);
            incidente.setMotivo(motivo);
            incidente.setPuntos(puntos);
            incidente.setInc_imgs(imgs2);

            Date currentTime = Calendar.getInstance().getTime();
            incidente.setInc_fecha(currentTime.getTime());
            db.child(FirebaseReference.INCIDENTES).child(incidente.getMotivo().getMot_tipo()).push().setValue(incidente);

            latLng = null;

        }
    } public void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione Imagenes"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ClipData mClipData = data.getClipData();

            int lenght = mClipData.getItemCount() >= 10 ? 10 : mClipData.getItemCount();

            for (int i = 0; i < lenght; i++) {
                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                uris.add(uri);

            }
            adapter = new AdaptadorImagenesHorizontal(uris);
            recycler.setAdapter(adapter);
        }
    }
    void subirAchivos(){
        imgs2 = new ArrayList();
for(Uri uri:uris){
        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("imagenes").child(uri.getLastPathSegment());

        reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {

                   urls.add(task.getResult().getDownloadUrl().toString());
                    if (urls.size() == 0)
                        finalizar();
                    for (int i = 0; i < urls.size(); i++) {

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                               .child(uris.get(i).getLastPathSegment());

                        storageReference.putFile(uris.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                   imgs2.add(task.getResult().getDownloadUrl().toString());
                                    if (imgs2.size() >= uris.size())
                                        finalizar();


                                }

                            }

                        });
                    }
                }

            }
        });
    }
}
}

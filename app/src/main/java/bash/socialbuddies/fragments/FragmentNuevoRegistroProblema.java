package bash.socialbuddies.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

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
    private Button coordenadas, enviar, imgs, buttonSeleccionar;
    private ArrayList<String> sp;
    private ArrayList<Uri> uris;
    private RecyclerView recycler;
    private ArrayList<String> urls;
    private AdaptadorImagenesHorizontal adapter;

    public static ArrayList<bash.socialbuddies.beans.BeanUbicacion> puntos;

    private String selected;

    BeanIncidente beanIncidente;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_problema, container, false);
        super.onCreate(savedInstanceState);
        uris = new ArrayList();

        titulo = (EditText) view.findViewById(R.id.fragment_registro_problema_titulo);
        descripcion = (EditText) view.findViewById(R.id.fragment_registro_problema_descripcion);
        coordenadas = (Button) view.findViewById(R.id.fragment_registro_problema_coordenadas);
        enviar = (Button) view.findViewById(R.id.fragment_registro_problema_enviar);
        imgs = (Button) view.findViewById(R.id.fragment_registro_problema_imgs);
        recycler = (RecyclerView) view.findViewById(R.id.fragment_registro_problema_imagnes);
        buttonSeleccionar = ((Button) view.findViewById(R.id.fragment_registro_problema_button_seleccionar));
        imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        sp = new ArrayList<String>();
        sp.add("Choques");
        sp.add("Socavon");
        sp.add("Inundacion");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirAchivos();
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


        buttonSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sp.toArray(new String[sp.size()])), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSeleccionar.setText(sp.get(which));
                        selected = sp.get(which);
                    }
                }).create().show();
            }
        });

        return view;
    }

    void validar() {
        finalizar();
    }

    void finalizar() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        BeanUsuario usuario = Singleton.getInstancia().getBeanUsuario();
        beanIncidente = new BeanIncidente();
        beanIncidente.setBeanUsuario(usuario);
        beanIncidente.setInc_fecha(System.currentTimeMillis());
        beanIncidente.setInc_descripcion(descripcion.getText().toString());
        beanIncidente.setInc_imgs(urls);
        beanIncidente.setUbicacion(latLng);
        beanIncidente.setPuntos(puntos);

        BeanMotivo motivo = new BeanMotivo();
        motivo.setMot_tipo(selected);
        motivo.setMot_titulo(titulo.getText().toString());

        db.child(FirebaseReference.INCIDENTES).child(motivo.getMot_tipo()).push().setValue(beanIncidente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    getActivity().finish();
                }
            }
        });
    }

    public void seleccionarImagen() {
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

    private void subirAchivos() {

        final int[] i = {0};
        urls = new ArrayList<>();

        for (final Uri uri : uris) {
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("imagenes").child(uri.getLastPathSegment());
            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    i[0]++;
                    if (task.isSuccessful()) {
                        urls.add(task.getResult().getDownloadUrl().toString());
                    }

                    if (i[0] >= uris.size()) {
                        finalizar();
                    }
                }
            });
        }
    }
}

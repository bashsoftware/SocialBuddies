package bash.socialbuddies.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.adapters.AdaptadorComentarios;
import bash.socialbuddies.beans.BeanComentario;
import bash.socialbuddies.utilities.CodeUtilities;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class FragmentContenidoComentario extends Fragment {

    public static final int Evento = 1;
    public static int opcion;
    private RecyclerView recyclerView;
    private EditText editText;
    private FloatingActionButton floatingActionButton;

    private AdaptadorComentarios adaptador;
    private ArrayList<BeanComentario> list;

    private String idPublicacion;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contenido_comentarios, container, false);

        recyclerView = ((RecyclerView) view.findViewById(R.id.fragment_contenido_comentarios_recyclerView));
        editText = ((EditText) view.findViewById(R.id.fragment_contenido_comentarios_editText_comentario));
        floatingActionButton = ((FloatingActionButton) view.findViewById(R.id.fragment_contenido_comentarios_floatingActionButton));

        list = new ArrayList<>();
        adaptador = new AdaptadorComentarios(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptador);

        Bundle bundle = getActivity().getIntent().getExtras();
        idPublicacion = bundle.getString(ActivityContenido.PUBLICACION_COMENTARIOS_FILTRO);

        if (idPublicacion == null) {
            getActivity().finish();
            return null;
        }

        cargarDatos();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoComentario();
            }
        });

        return view;
    }

    private void cargarDatos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_COMENTARIO);

        reference.child(idPublicacion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BeanComentario beanComentario = snapshot.getValue(BeanComentario.class);
                    beanComentario.setCom_id(snapshot.getKey());
                    list.add(beanComentario);
                }

                adaptador.update(list);
                cargarMeGusta();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void nuevoComentario() {

        CodeUtilities.cerrarTeclado(getView(), getActivity());

        if (!editText.getText().toString().equals("")) {

            BeanComentario beanComentario = new BeanComentario();
            beanComentario.setBeanUsuario(Singleton.getInstancia().getBeanUsuario());
            beanComentario.setCom_contenido(editText.getText().toString());
            beanComentario.setCom_fecha(System.currentTimeMillis());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_COMENTARIO);
            reference.child(idPublicacion).push().setValue(beanComentario).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        adaptador.update(list);
                    } else {
                        Snackbar.make(getView(), "Error al publicar, inténtelo nuevamente", Snackbar.LENGTH_SHORT).setAction("Reintentar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nuevoComentario();
                            }
                        }).show();
                    }
                }
            });

        }
    }

    private void cargarMeGusta() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_COMENTARIO_LIKE);

        for (int i = 0; i < list.size(); i++) {
            final BeanComentario comentario = list.get(i);

            final int finalI = i;
            reference.child(comentario.getCom_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long l = dataSnapshot.getChildrenCount();
                    comentario.setNumMeGusta(Integer.valueOf(l + ""));

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().toString().equals(Singleton.getInstancia().getBeanUsuario().getUsu_id())) {
                            comentario.setMeGusta(true);
                            break;
                        }
                    }
                    list.set(finalI, comentario);
                    adaptador.update(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}

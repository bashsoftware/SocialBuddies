package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.adapters.AdaptadorPublicaciones;
import bash.socialbuddies.beans.BeanPublicacion;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class FragmentContenidoPublicaciones extends Fragment {

    String id;
    BeanUsuario beanUsuario;
    long count = 0;

    private View view;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<BeanPublicacion> list;
    private AdaptadorPublicaciones adaptador;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contenido_lista_recycler, container, false);

        recyclerView = ((RecyclerView) view.findViewById(R.id.fragment_contenido_lista_recycler_reciclerView));
        floatingActionButton = ((FloatingActionButton) view.findViewById(R.id.fragment_contenido_lista_recycler_fab));

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new AdaptadorPublicaciones(list);
        recyclerView.setAdapter(adaptador);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityContenido.class);
                intent.putExtra(ActivityContenido.ESTATUS, ActivityContenido.PUBLICACION_REGISTRO);
                startActivity(intent);
            }
        });

        beanUsuario = Singleton.getInstancia().getBeanUsuario();

        loadDada();

        return view;
    }

    private void loadDada() {

        list = new ArrayList<>();
        count = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACIONES);

        if (id != null) {
            reference = reference.child(id);
        }

        reference.orderByChild("pub_fecha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (id != null) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                            final BeanPublicacion beanPublicacion = snapshot1.getValue(BeanPublicacion.class);
                            beanPublicacion.setIdPublicacion(snapshot1.getKey());
                            list.add(beanPublicacion);
                        }
                    }
                } else {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                            final BeanPublicacion beanPublicacion = snapshot1.getValue(BeanPublicacion.class);
                            beanPublicacion.setIdPublicacion(snapshot1.getKey());
                            list.add(beanPublicacion);
                        }
                    }
                }

                Collections.reverse(list);
                adaptador.update(list);
                actualizarLikes();
                actualizarComentarios();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void actualizarLikes() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_LIKE);

        for (int i = 0; i < list.size(); i++) {

            final BeanPublicacion publicacion = list.get(i);
            final int finalI = i;

            reference.child(publicacion.getIdPublicacion()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long l = dataSnapshot.getChildrenCount();
                    publicacion.setNumLikes(Integer.parseInt(l + ""));

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().toString().equals(Singleton.getInstancia().getBeanUsuario().getUsu_id())) {
                            publicacion.setMeGusta(true);
                            break;
                        }
                    }

                    list.set(finalI, publicacion);
                    adaptador.update(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void actualizarComentarios() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_COMENTARIO);

        for (int i = 0; i < list.size(); i++) {

            final BeanPublicacion publicacion = list.get(i);
            final int finalI = i;

            reference.child(publicacion.getIdPublicacion()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long l = dataSnapshot.getChildrenCount();
                    publicacion.setNumComentarios(Integer.parseInt(l + ""));
                    list.set(finalI, publicacion);
                    adaptador.update(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}

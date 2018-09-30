package bash.socialbuddies.fragments;

import android.support.v7.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bash.socialbuddies.adapters.AdapterTipoMotivo;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.interfaces.OnCallBackBusqueda;
import bash.socialbuddies.utilities.FirebaseReference;

public class DataBaseUtil {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    public void obtieneTiposMotivos(final FragmentBusqueda callback){
        DatabaseReference ref = database.getReference().child(FirebaseReference.MOTIVO);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                final ArrayList<BeanMotivo> motivos = new ArrayList<>();

                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                    BeanMotivo post = dataSnapshot.getValue(BeanMotivo.class);
                    motivos.add(post);
                }

                callback.onGetTipoMotivos(motivos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    public void obtieneIncidentes(final FragmentBusqueda callback){
        DatabaseReference ref = database.getReference().child(FirebaseReference.INCIDENTES);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                final ArrayList<BeanIncidente> incidentes = new ArrayList<>();

                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                    BeanIncidente post = dataSnapshot.getValue(BeanIncidente.class);
                    incidentes.add(post);
                }

                callback.onGetIncidentes(incidentes);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    /*
    *
         DatabaseReference ref = database.getReference().child(FirebaseReference.MOTIVOS);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                final ArrayList<BeanMotivo> motivos = new ArrayList<>();

                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                    BeanMotivo post = dataSnapshot.getValue(BeanMotivo.class);
                    motivos.add(post);
                }

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                _recyclerTipos.setLayoutManager(horizontalLayoutManager);
                _adapterTipoMotivo = new AdapterTipoMotivo(getActivity().getApplicationContext(), motivos);
                _recyclerTipos.setAdapter(_adapterTipoMotivo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference ref = database.getReference().child(FirebaseReference.MOTIVOS);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                final ArrayList<BeanMotivo> motivos = new ArrayList<>();

                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                    BeanMotivo post = dataSnapshot.getValue(BeanMotivo.class);
                    motivos.add(post);
                }

                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                _recyclerTipos.setLayoutManager(horizontalLayoutManager);
                _adapterTipoMotivo = new AdapterTipoMotivo(getActivity().getApplicationContext(), motivos);
                _recyclerTipos.setAdapter(_adapterTipoMotivo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    *
    * */
}

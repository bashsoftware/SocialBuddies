package bash.socialbuddies.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import bash.socialbuddies.R;
import bash.socialbuddies.adapters.AdapterIncidenteLista;
import bash.socialbuddies.adapters.AdapterTipoMotivo;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.interfaces.*;
import bash.socialbuddies.utilities.FirebaseReference;

public class FragmentBusqueda extends Fragment implements OnCallBackBusqueda {
    private RecyclerView _recyclerTipos;
    private RecyclerView _recyclerIncidentes;
    private AdapterTipoMotivo _adapterTipoMotivo;
    private AdapterIncidenteLista _adapterIncidentes;
    private DataBaseUtil dbUtil;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        super.onCreate(savedInstanceState);

        _recyclerTipos = view.findViewById(R.id.rwFrgBusTipos);
        _recyclerIncidentes = view.findViewById(R.id.rwFrgIncLista);
        initDataBase();

        return  view;
    }

    private void initDataBase(){
        dbUtil = new DataBaseUtil();
        dbUtil.obtieneIncidentes(this);
        dbUtil.obtieneTiposMotivos(this);

    }

    @Override
    public void onGetTipoMotivos(ArrayList<BeanMotivo> arrayMotivos) {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        _recyclerTipos.setLayoutManager(horizontalLayoutManager);
        _adapterTipoMotivo = new AdapterTipoMotivo(getActivity().getApplicationContext(), arrayMotivos);
        _recyclerTipos.setAdapter(_adapterTipoMotivo);
    }

    @Override
    public void onGetIncidentes(ArrayList<BeanIncidente> arrayIncidentes) {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        _recyclerIncidentes.setLayoutManager(horizontalLayoutManager);
        _adapterIncidentes = new AdapterIncidenteLista(getActivity().getApplicationContext(), arrayIncidentes);
        _recyclerIncidentes.setAdapter(_adapterIncidentes);

    }
}


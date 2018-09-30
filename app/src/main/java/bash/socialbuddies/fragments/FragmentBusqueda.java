package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.adapters.AdapterIncidenteLista;
import bash.socialbuddies.adapters.AdapterTipoMotivo;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.interfaces.OnCallBackBusqueda;
import bash.socialbuddies.interfaces.OnCallBackSelTipo;

public class FragmentBusqueda extends Fragment implements OnCallBackBusqueda, OnCallBackSelTipo {
    private RecyclerView _recyclerTipos;
    private ListView listView;
    private AdapterTipoMotivo _adapterTipoMotivo;
    private AdapterIncidenteLista _adapterIncidentes;
    private DataBaseUtil dbUtil;

    private ArrayList<BeanIncidente> beanIncidentes;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        super.onCreate(savedInstanceState);

        _recyclerTipos = view.findViewById(R.id.rwFrgBusTipos);
        listView = view.findViewById(R.id.rwFrgIncLista);
        initDataBase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ActivityContenido.class);

                Bundle bundle = new Bundle();
                bundle.putInt(ActivityContenido.ESTATUS, ActivityContenido.INCIDENTE);
                bundle.putSerializable(ActivityContenido.INCIDENTE_FILTRO, beanIncidentes.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        return view;
    }

    private void initDataBase() {
        dbUtil = new DataBaseUtil();
        dbUtil.obtieneIncidentes(this, "");
        dbUtil.obtieneTiposMotivos(this);
    }

    @Override
    public void onGetTipoMotivos(ArrayList<BeanMotivo> arrayMotivos) {
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        _recyclerTipos.setLayoutManager(horizontalLayoutManager);
        _adapterTipoMotivo = new AdapterTipoMotivo(getActivity().getApplicationContext(), arrayMotivos, this);
        _recyclerTipos.setAdapter(_adapterTipoMotivo);
    }

    @Override
    public void onGetIncidentes(ArrayList<BeanIncidente> arrayIncidentes) {
        beanIncidentes = arrayIncidentes;
        _adapterIncidentes = new AdapterIncidenteLista(getContext(), beanIncidentes);
        listView.setAdapter(_adapterIncidentes);
        _adapterIncidentes.notifyDataSetChanged();
    }

    @Override
    public void onSelectTipo(String clave) {
        _adapterIncidentes.clear();
        dbUtil.obtieneIncidentes(this, clave);
    }
}


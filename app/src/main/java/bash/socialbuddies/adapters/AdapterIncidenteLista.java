package bash.socialbuddies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;

public class AdapterIncidenteLista extends RecyclerView.Adapter<AdapterIncidenteLista.IncidenteView> {
    private ArrayList<BeanIncidente> _incidentesArray;
    private Context _context;
    private LayoutInflater _inflater;

    public AdapterIncidenteLista(Context context, ArrayList<BeanIncidente> incidentes) {
        _inflater = LayoutInflater.from(context);
        _context = context;
        _incidentesArray = incidentes;
    }

    @NonNull
    @Override
    public AdapterIncidenteLista.IncidenteView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.incidente_row, parent, false);
        return new AdapterIncidenteLista.IncidenteView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIncidenteLista.IncidenteView viewHolder, int position) {
        BeanIncidente ins = _incidentesArray.get(position);
        viewHolder._txtTitulo.setText(_incidentesArray.get(position).getInc_titulo());
        viewHolder._txtDescripcion.setText(_incidentesArray.get(position).getInc_descripcion());
        viewHolder._txtUsuNombre.setText(_incidentesArray.get(position).getUsuario().getUsu_nombre());
        viewHolder._txtFecha.setText(_incidentesArray.get(position).getInc_fecha());
    }

    @Override
    public int getItemCount() {
        return _incidentesArray.size();
    }


    public class IncidenteView extends RecyclerView.ViewHolder {
        View _view;
        TextView _txtTitulo;
        TextView _txtDescripcion;
        TextView _txtMotivo;
        TextView _txtUsuNombre;
        TextView _txtFecha;

        public IncidenteView(View itemView) {
            super(itemView);
            _view = itemView;
            _txtTitulo = _view.findViewById(R.id.txtIncTitulo);
            _txtDescripcion = _view.findViewById(R.id.txtIncDescripcion);
            _txtFecha = _view.findViewById(R.id.txtIncFecha);
            _txtUsuNombre = _view.findViewById(R.id.txtIncUsuario);
        }
    }
}


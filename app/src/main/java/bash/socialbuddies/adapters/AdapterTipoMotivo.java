package bash.socialbuddies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanMotivo;
import bash.socialbuddies.fragments.FragmentBusqueda;
import bash.socialbuddies.interfaces.OnCallBackSelTipo;

public class AdapterTipoMotivo extends RecyclerView.Adapter<AdapterTipoMotivo.TipoListaView> {
    private ArrayList<BeanMotivo> _motivosArray = new ArrayList<>();
    private Context _context;
    private LayoutInflater _inflater;
    private OnCallBackSelTipo _callBack;
    public AdapterTipoMotivo(Context context, ArrayList<BeanMotivo> motivos, FragmentBusqueda callBack) {
        _inflater = LayoutInflater.from(context);
        _context = context;
        _motivosArray = motivos;
        _callBack = callBack;
    }

    @NonNull
    @Override
    public AdapterTipoMotivo.TipoListaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.tipos_motivos_row, parent, false);
        return new TipoListaView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTipoMotivo.TipoListaView viewHolder, final int position) {
        //viewHolder._imgIcono.setB
        viewHolder._txtNombre.setText(_motivosArray.get(position).getMot_titulo());

        viewHolder._imgIcono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _callBack.onSelectTipo(_motivosArray.get(position).getMot_tipo());
                //Toast.makeText(_context, "dawd" + _motivosArray.get(position).getMot_tipo(), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _motivosArray.size();
    }


    public class TipoListaView extends RecyclerView.ViewHolder {
        View _view;
        ImageView _imgIcono;
        TextView _txtNombre;

        public TipoListaView(View itemView) {
            super(itemView);
            _view = itemView;
            _imgIcono = _view.findViewById(R.id.imgTipoIcono);
            _txtNombre = _view.findViewById(R.id.txtTipoNombre);
        }
    }

};
package bash.socialbuddies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanMotivo;

public class AdapterTipoMotivo extends RecyclerView.Adapter<AdapterTipoMotivo.TipoListaView> {
    private ArrayList<BeanMotivo> _motivosArray = new ArrayList<>();
    private Context _context;
    private LayoutInflater _inflater;

    public AdapterTipoMotivo(Context context, ArrayList<BeanMotivo> motivos) {
        _inflater = LayoutInflater.from(context);
        _context = context;
        _motivosArray = motivos;
    }

    @NonNull
    @Override
    public AdapterTipoMotivo.TipoListaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.tipos_motivos_row, parent, false);
        return new TipoListaView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTipoMotivo.TipoListaView viewHolder, int position) {
        //viewHolder._imgIcono.setB
        viewHolder._txtNombre.setText(_motivosArray.get(position).getMot_titulo());
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
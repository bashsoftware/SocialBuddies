package bash.socialbuddies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.util.List;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanIncidente;

public class AdapterIncidenteLista extends ArrayAdapter {

    Context context;

    public AdapterIncidenteLista(Context context, List beans) {
        super(context, 0, beans);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.incidente_row, parent, false);
        }

        TextView _txtTitulo = view.findViewById(R.id.txtIncTitulo);
        TextView _txtDescripcion = view.findViewById(R.id.txtIncDescripcion);
        TextView _txtFecha = view.findViewById(R.id.txtIncFecha);
        TextView _txtUsuNombre = view.findViewById(R.id.txtIncUsuario);
        ImageView _imgUsuario = view.findViewById(R.id.imgIncUsuario);

        BeanIncidente ins = ((BeanIncidente) getItem(position));

        _txtTitulo.setText(ins.getMotivo().getMot_titulo());
        _txtDescripcion.setText(ins.getInc_descripcion());
        _txtUsuNombre.setText(ins.getBeanUsuario().getUsu_nombre());
        _txtFecha.setText(DateFormat.getDateTimeInstance().format(ins.getInc_fecha()));
        Glide.with(context).load(ins.getBeanUsuario().getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(_imgUsuario);

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

}


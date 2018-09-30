package bash.socialbuddies.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanComentario;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class AdaptadorComentarios extends RecyclerView.Adapter<AdaptadorComentarios.ViewHolder> {

    protected Context context;
    protected ArrayList<BeanComentario> list;

    public AdaptadorComentarios(ArrayList<BeanComentario> list) {
        this.list = list;
    }

    @Override
    public AdaptadorComentarios.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comentario, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final BeanComentario beanComentario = list.get(position);

        if (beanComentario != null && beanComentario.getBeanUsuario() != null) {
            BeanUsuario beanUsuario = beanComentario.getBeanUsuario();

            Glide.with(context).load(beanUsuario.getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(holder.imageView_fotoPerfil);
            holder.textview_nombre.setText(beanUsuario.getUsu_nombre() != null ? beanUsuario.getUsu_nombre() : "");
            holder.textview_contenido.setText(beanComentario.getCom_contenido() != null ? beanComentario.getCom_contenido() : "");
            holder.textview_fecha.setText(beanComentario.getCom_fecha() != null ? beanComentario.getCom_fecha().toString() : "");
            holder.textview_numMeGusta.setText(beanComentario.getNumMeGusta() != null && beanComentario.getNumMeGusta() > 0 ? beanComentario.getNumMeGusta() + " Me Gusta" : "");
            holder.button_meGusta.setImageResource(beanComentario.getMeGusta() != null && beanComentario.getMeGusta() ? R.drawable.ic_like : R.drawable.ic_like_border);
            holder.button_meGusta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (beanComentario.getMeGusta() != null && beanComentario.getMeGusta()) {
                        setMegusta(beanComentario, false);
                        beanComentario.setMeGusta(false);
                    } else {
                        setMegusta(beanComentario, true);
                        beanComentario.setMeGusta(true);
                    }

                    notifyDataSetChanged();
                }
            });
        } else {
            list.remove(position);
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(List<BeanComentario> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setMegusta(BeanComentario beanComentario, boolean meGusta) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_COMENTARIO_LIKE).child(beanComentario.getCom_id());
        if (meGusta) {
            reference.child(Singleton.getInstancia().getBeanUsuario().getUsu_id()).setValue(true);
        } else {
            reference.child(Singleton.getInstancia().getBeanUsuario().getUsu_id()).setValue(null);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_fotoPerfil;
        TextView textview_nombre;
        TextView textview_contenido;
        TextView textview_fecha;
        TextView textview_numMeGusta;
        ImageButton button_meGusta;

        public ViewHolder(View v) {
            super(v);
            imageView_fotoPerfil = ((ImageView) v.findViewById(R.id.list_item_comentario_imageView_fotoPerfil));
            textview_nombre = ((TextView) v.findViewById(R.id.list_item_comentario_textview_nombre));
            textview_contenido = ((TextView) v.findViewById(R.id.list_item_comentario_textview_contenido));
            textview_fecha = ((TextView) v.findViewById(R.id.list_item_comentario_textview_fecha));
            textview_numMeGusta = ((TextView) v.findViewById(R.id.list_item_comentario_textview_numMeGusta));
            button_meGusta = ((ImageButton) v.findViewById(R.id.list_item_comentario_button_meGusta));
        }
    }
}

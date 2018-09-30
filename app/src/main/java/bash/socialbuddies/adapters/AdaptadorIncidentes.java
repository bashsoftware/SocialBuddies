package bash.socialbuddies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.activities.ActivityMapa;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class AdaptadorIncidentes extends RecyclerView.Adapter<AdaptadorIncidentes.ViewHolder> {

    private Context context;
    private ArrayList<BeanIncidente> items;

    public AdaptadorIncidentes(ArrayList<BeanIncidente> items) {
        this.items = items;
    }

    @Override
    public AdaptadorIncidentes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_publicacion, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BeanIncidente beanIncidente = items.get(position);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, beanIncidente.getInc_imgs());
        final int length = beanIncidente.getInc_imgs() != null ? beanIncidente.getInc_imgs().size() : 0;
        holder.viewPagerImagenes.setAdapter(viewPagerAdapter);
        holder.frameLayout.setVisibility(View.GONE);

        Glide.with(context).load(beanIncidente.getBeanUsuario().getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewFotoPerfil);
        holder.textViewNombre.setText(beanIncidente.getBeanUsuario().getUsu_nombre());
        holder.textViewDescripcion.setText(beanIncidente.getInc_descripcion() != null ? beanIncidente.getInc_descripcion() : "");
        holder.textViewFecha.setText(beanIncidente.getInc_fecha() != null ? new java.util.Date(beanIncidente.getInc_fecha()).toString() : "");
        holder.buttonlike.setImageResource(beanIncidente.getMeGusta() != null && beanIncidente.getMeGusta() ? R.drawable.ic_like : R.drawable.ic_like_border);
        holder.textViewNumLikes.setText(beanIncidente.getNumLikes() != null && beanIncidente.getNumLikes() > 0 ? beanIncidente.getNumLikes() + " Me gusta" : "");
        holder.textViewNumComentarios.setText((beanIncidente.getNumComentarios() != null && beanIncidente.getNumComentarios() > 0) ? (beanIncidente.getNumComentarios() + (beanIncidente.getNumComentarios() == 1 ? " Comentario" : " Comentarios")) : "");

        holder.textViewDescripcion.setVisibility(holder.textViewDescripcion.getText().toString().equals("") ? View.GONE : View.VISIBLE);

        holder.textViewTitulo.setText(beanIncidente.getMotivo().getMot_tipo());
        String s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(beanIncidente.getInc_fecha());
        holder.textViewFecha.setText(s);

        holder.textViewNumComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanIncidente);
            }
        });
        holder.buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ActivityMapa.class);
                i.putExtra("lat",beanIncidente.getUbicacion().getLat());
                i.putExtra("lng",beanIncidente.getUbicacion().getLng());
                ActivityMapa.puntos = beanIncidente.getPuntos() ;
                context.startActivity(i);
            }
        });

        holder.buttonlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean b = beanIncidente.getMeGusta();
                if (b != null && b) {
                    holder.buttonlike.setImageResource(R.drawable.ic_like_border);
                    beanIncidente.setMeGusta(false);
                    likePublicación(false, beanIncidente, Singleton.getInstancia().getBeanUsuario());
                    beanIncidente.setNumLikes(beanIncidente.getNumLikes() - 1);
                } else {
                    holder.buttonlike.setImageResource(R.drawable.ic_like);
                    beanIncidente.setMeGusta(true);
                    likePublicación(true, beanIncidente, Singleton.getInstancia().getBeanUsuario());
                    beanIncidente.setNumLikes(beanIncidente.getNumLikes() + 1);
                }
            }
        });

        holder.buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanIncidente);
            }
        });

        if (length == 1) {
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.textViewNumImagenes.setVisibility(View.GONE);
        } else if (length > 1) {
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.textViewNumImagenes.setText("1/" + length);
            holder.viewPagerImagenes.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    holder.textViewNumImagenes.setText(position + 1 + "/" + length);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return items == null ? 0 : items.size();
    }

    private void likePublicación(boolean like, BeanIncidente beanPublicacion, BeanUsuario beanUsuario) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_LIKE);

        if (like) {
            reference.child(beanPublicacion.getInc_id()).child(beanUsuario.getUsu_id()).setValue(true);
        } else {
            reference.child(beanPublicacion.getInc_id()).child(beanUsuario.getUsu_id()).setValue(null);
        }
    }

    private void verComentarios(BeanIncidente beanPublicacion) {
        Intent intent = new Intent(context, ActivityContenido.class);

        intent.putExtra(ActivityContenido.ESTATUS, ActivityContenido.PUBLICACION_COMENTARIOS);
        intent.putExtra(ActivityContenido.PUBLICACION_COMENTARIOS_FILTRO, beanPublicacion.getInc_id());
        context.startActivity(intent);
    }

    public void update(List<BeanIncidente> newlist) {
        this.items.clear();
        this.items.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ViewPager viewPagerImagenes;
        ImageView imageViewFotoPerfil;
        TextView textViewTitulo;
        TextView textViewNumImagenes;
        TextView textViewNombre;
        TextView textViewDescripcion;
        TextView textViewNumComentarios;
        TextView textViewNumLikes;
        TextView textViewFecha;
        Button buttonMapa;
        ImageButton buttonComentar;
        ImageButton buttonlike;

        public ViewHolder(View view) {
            super(view);
            frameLayout = ((FrameLayout) view.findViewById(R.id.fragment_contenido_incidente_frameLayout_imagenes));
            viewPagerImagenes = ((ViewPager) view.findViewById(R.id.fragment_contenido_incidente_viewpager));
            imageViewFotoPerfil = ((ImageView) view.findViewById(R.id.fragment_contenido_incidente_imagenPerfil));
            textViewNumImagenes = view.findViewById(R.id.fragment_contenido_incidente_numero);
            textViewTitulo = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textView_titulo));
            textViewNombre = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textView_nombreProveedor));
            textViewDescripcion = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textoPublicacion));
            textViewNumComentarios = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textoNumComentarios));
            textViewNumLikes = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textoNumLikes));
            textViewFecha = ((TextView) view.findViewById(R.id.fragment_contenido_incidente_textView_fecha));
            buttonComentar = ((ImageButton) view.findViewById(R.id.fragment_contenido_incidente_button_comentar));
            buttonlike = ((ImageButton) view.findViewById(R.id.fragment_contenido_incidente_button_like));
            buttonMapa = ((Button) view.findViewById(R.id.fragment_contenido_incidente_button_mapa));
        }

    }


}




package bash.socialbuddies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.beans.BeanPublicacion;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class AdaptadorPublicaciones extends RecyclerView.Adapter<AdaptadorPublicaciones.ViewHolder> {

    private Context context;
    private ArrayList<BeanPublicacion> items;

    public AdaptadorPublicaciones(ArrayList<BeanPublicacion> items) {
        this.items = items;
    }

    @Override
    public AdaptadorPublicaciones.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_publicacion, parent, false);
        ViewHolder vh = new ViewHolder(v, context, items);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, items.get(position).getBeanPublicacionImagenes());
        final int length = items.get(position).getBeanPublicacionImagenes() != null ? items.get(position).getBeanPublicacionImagenes().size() : 0;

        final BeanPublicacion beanPublicacion = items.get(position);

        //EVENTOS GENERALES=========================================================================
        holder.viewPagerImagenes.setAdapter(viewPagerAdapter);
        holder.frameLayout.setVisibility(View.GONE);

        Glide.with(context).load(beanPublicacion.getBeanUsuario().getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(holder.imageViewFotoPerfil);
        holder.textViewNombre.setText(beanPublicacion.getBeanUsuario().getUsu_nombre());
        holder.textViewDescripcion.setText(beanPublicacion.getPub_descripcion() != null ? beanPublicacion.getPub_descripcion() : "");
        holder.textViewFecha.setText(beanPublicacion.getPub_fecha() != null ? new Date(beanPublicacion.getPub_fecha()).toString() : "");
        holder.buttonlike.setImageResource(beanPublicacion.getMeGusta() != null && beanPublicacion.getMeGusta() ? R.drawable.ic_like : R.drawable.ic_like_border);
        holder.textViewNumLikes.setText(beanPublicacion.getNumLikes() != null && beanPublicacion.getNumLikes() > 0 ? beanPublicacion.getNumLikes() + " Me gusta" : "");
        holder.textViewNumComentarios.setText((beanPublicacion.getNumComentarios() != null && beanPublicacion.getNumComentarios() > 0) ? (beanPublicacion.getNumComentarios() + (beanPublicacion.getNumComentarios() == 1 ? " Comentario" : " Comentarios")) : "");

        holder.textViewDescripcion.setVisibility(holder.textViewDescripcion.getText().toString().equals("") ? View.GONE : View.VISIBLE);

        holder.textViewNumComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanPublicacion);
            }
        });

        holder.buttonlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean b = items.get(position).getMeGusta();
                if (b != null && b) {
                    holder.buttonlike.setImageResource(R.drawable.ic_like_border);
                    items.get(position).setMeGusta(false);
                    likePublicación(false, beanPublicacion, Singleton.getInstancia().getBeanUsuario());
                    beanPublicacion.setNumLikes(beanPublicacion.getNumLikes() - 1);
                    items.set(position, beanPublicacion);
                } else {
                    holder.buttonlike.setImageResource(R.drawable.ic_like);
                    items.get(position).setMeGusta(true);
                    likePublicación(true, beanPublicacion, Singleton.getInstancia().getBeanUsuario());
                    beanPublicacion.setNumLikes(beanPublicacion.getNumLikes() + 1);
                    items.set(position, beanPublicacion);
                }
                notifyDataSetChanged();
            }
        });

        holder.buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanPublicacion);
            }
        });

        //EVENTOS ESPECIALES========================================================================

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

    private void likePublicación(boolean like, BeanPublicacion beanPublicacion, BeanUsuario beanUsuario) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACION_LIKE);

        if (like) {
            reference.child(beanPublicacion.getIdPublicacion()).child(beanUsuario.getUsu_id()).setValue(true);
        } else {
            reference.child(beanPublicacion.getIdPublicacion()).child(beanUsuario.getUsu_id()).setValue(null);
        }
    }

    private void verComentarios(BeanPublicacion beanPublicacion) {
        Intent intent = new Intent(context, ActivityContenido.class);

        intent.putExtra(ActivityContenido.ESTATUS, ActivityContenido.PUBLICACION_COMENTARIOS);
        intent.putExtra(ActivityContenido.PUBLICACION_COMENTARIOS_FILTRO, beanPublicacion.getIdPublicacion());
        context.startActivity(intent);
    }

    public void update(List<BeanPublicacion> newlist) {
        this.items.clear();
        this.items.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ViewPager viewPagerImagenes;
        ImageView imageViewFotoPerfil;
        TextView textViewNumImagenes;
        TextView textViewNombre;
        TextView textViewDescripcion;
        TextView textViewNumComentarios;
        TextView textViewNumLikes;
        TextView textViewFecha;
        ImageButton buttonComentar;
        ImageButton buttonlike;

        public ViewHolder(View view, Context context, ArrayList<BeanPublicacion> items) {
            super(view);
            frameLayout = ((FrameLayout) view.findViewById(R.id.list_item_publicacion_frameLayout_imagenes));
            viewPagerImagenes = ((ViewPager) view.findViewById(R.id.list_item_publicacion_viewpager));
            imageViewFotoPerfil = ((ImageView) view.findViewById(R.id.list_item_publicacion_imagenPerfil));
            textViewNumImagenes = view.findViewById(R.id.list_item_publicacion_numero);
            textViewNombre = ((TextView) view.findViewById(R.id.list_item_publicacion_textView_nombreProveedor));
            textViewDescripcion = ((TextView) view.findViewById(R.id.list_item_publicacion_textoPublicacion));
            textViewNumComentarios = ((TextView) view.findViewById(R.id.list_item_publicacion_textoNumComentarios));
            textViewNumLikes = ((TextView) view.findViewById(R.id.list_item_publicacion_textoNumLikes));
            textViewFecha = ((TextView) view.findViewById(R.id.list_item_publicacion_textView_fecha));
            buttonComentar = ((ImageButton) view.findViewById(R.id.list_item_publicacion_button_comentar));
            buttonlike = ((ImageButton) view.findViewById(R.id.list_item_publicacion_button_like));
        }

    }

    public class ViewPagerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<String> images;

        public ViewPagerAdapter(Context context, List<String> images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images != null ? images.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_item_publicacion_content, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_item_publicacion_content_imagen);
            Glide.with(context).load(images.get(position)).into(imageView);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);
        }
    }
}




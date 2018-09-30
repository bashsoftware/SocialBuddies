package bash.socialbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import java.util.Date;

import bash.socialbuddies.R;
import bash.socialbuddies.activities.ActivityContenido;
import bash.socialbuddies.adapters.ViewPagerAdapter;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class FragmentContenidoIncidente extends Fragment {

    private View view;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contenido_incidente, container, false);

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

        Bundle bundle = getActivity().getIntent().getExtras();
        BeanIncidente beanIncidente = (BeanIncidente) bundle.getSerializable(ActivityContenido.PUBLICACION_FILTRO);

        if (beanIncidente != null) {
            cargarDatos(beanIncidente);
        } else {
            getActivity().finish();
        }

        return view;
    }

    private void cargarDatos(final BeanIncidente beanIncidente) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), beanIncidente.getInc_imgs());
        final int length = beanIncidente.getInc_imgs() != null ? beanIncidente.getInc_imgs().size() : 0;
        viewPagerImagenes.setAdapter(viewPagerAdapter);
        frameLayout.setVisibility(View.GONE);

        Glide.with(getContext()).load(beanIncidente.getUsuario().getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(imageViewFotoPerfil);
        textViewNombre.setText(beanIncidente.getUsuario().getUsu_nombre());
        textViewDescripcion.setText(beanIncidente.getInc_descripcion() != null ? beanIncidente.getInc_descripcion() : "");
        textViewFecha.setText(beanIncidente.getInc_fecha() != null ? new Date(beanIncidente.getInc_fecha()).toString() : "");
        buttonlike.setImageResource(beanIncidente.getMeGusta() != null && beanIncidente.getMeGusta() ? R.drawable.ic_like : R.drawable.ic_like_border);
        textViewNumLikes.setText(beanIncidente.getNumLikes() != null && beanIncidente.getNumLikes() > 0 ? beanIncidente.getNumLikes() + " Me gusta" : "");
        textViewNumComentarios.setText((beanIncidente.getNumComentarios() != null && beanIncidente.getNumComentarios() > 0) ? (beanIncidente.getNumComentarios() + (beanIncidente.getNumComentarios() == 1 ? " Comentario" : " Comentarios")) : "");

        textViewDescripcion.setVisibility(textViewDescripcion.getText().toString().equals("") ? View.GONE : View.VISIBLE);

        textViewNumComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanIncidente);
            }
        });

        buttonlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean b = beanIncidente.getMeGusta();
                if (b != null && b) {
                    buttonlike.setImageResource(R.drawable.ic_like_border);
                    beanIncidente.setMeGusta(false);
                    likePublicación(false, beanIncidente, Singleton.getInstancia().getBeanUsuario());
                    beanIncidente.setNumLikes(beanIncidente.getNumLikes() - 1);
                } else {
                    buttonlike.setImageResource(R.drawable.ic_like);
                    beanIncidente.setMeGusta(true);
                    likePublicación(true, beanIncidente, Singleton.getInstancia().getBeanUsuario());
                    beanIncidente.setNumLikes(beanIncidente.getNumLikes() + 1);
                }
            }
        });

        buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verComentarios(beanIncidente);
            }
        });

        if (length == 1) {
            frameLayout.setVisibility(View.VISIBLE);
            textViewNumImagenes.setVisibility(View.GONE);
        } else if (length > 1) {
            frameLayout.setVisibility(View.VISIBLE);
            textViewNumImagenes.setText("1/" + length);
            viewPagerImagenes.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    textViewNumImagenes.setText(position + 1 + "/" + length);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Detalle");
    }

    private void verComentarios(BeanIncidente beanIncidente) {
        Intent intent = new Intent(getContext(), ActivityContenido.class);
        intent.putExtra(ActivityContenido.ESTATUS, ActivityContenido.PUBLICACION_COMENTARIOS);
        intent.putExtra(ActivityContenido.PUBLICACION_COMENTARIOS_FILTRO, beanIncidente.getInc_id());
        getActivity().startActivity(intent);
    }

    private void likePublicación(boolean like, BeanIncidente beanPublicacion, BeanUsuario beanUsuario) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.INCIDENTES);

        if (like) {
            reference.child(beanPublicacion.getMotivo().getMot_tipo()).child(beanPublicacion.getInc_id()).child(beanUsuario.getUsu_id()).setValue(true);
        } else {
            reference.child(beanPublicacion.getMotivo().getMot_tipo()).child(beanPublicacion.getInc_id()).child(beanUsuario.getUsu_id()).setValue(null);
        }
    }

}

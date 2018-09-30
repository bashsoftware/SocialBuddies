package bash.socialbuddies.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import bash.socialbuddies.R;
import bash.socialbuddies.fragments.FragmentContenidoComentario;
import bash.socialbuddies.fragments.FragmentContenidoPublicacionRegistro;

public class ActivityContenido extends AppCompatActivity {

    public static final String ESTATUS = "ESTATUS";

    public static final int PUBLICACION_COMENTARIOS = 1;
    public static final int PUBLICACION_REGISTRO = 2;

    public static final String PUBLICACION_COMENTARIOS_FILTRO = "PUBLICACION_COMENTARIOS_FILTRO";

    private FrameLayout frameLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);

        frameLayout = ((FrameLayout) findViewById(R.id.activity_contenido_frameLayout));
        toolbar = ((Toolbar) findViewById(R.id.activity_contenido_toolbar));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        displaySelectedScreen(bundle.getInt(ESTATUS));
    }

    public void displaySelectedScreen(int estatus) {
        Fragment fragment = null;

        switch (estatus) {
            case PUBLICACION_COMENTARIOS:
                fragment = new FragmentContenidoComentario();
                getSupportActionBar().setTitle("Comentarios");
                break;
            case PUBLICACION_REGISTRO:
                fragment = new FragmentContenidoPublicacionRegistro();
                getSupportActionBar().setTitle("Nueva Publicación");
                break;
            default:
                finish();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(frameLayout.getId(), fragment);
            ft.commit();
        }
    }

}

package bash.socialbuddies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.fragments.FragmentBusqueda;
import bash.socialbuddies.fragments.FragmentContenidoPublicaciones;
import bash.socialbuddies.fragments.FragmentNuevoRegistroProblema;
import bash.socialbuddies.fragments.FragmentVacio;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    Fragment fragment;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        frameLayout = (FrameLayout) findViewById(R.id.containt_main_framelayout);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            logout();
            return;
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.USUARIOS);
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeanUsuario beanUsuario = dataSnapshot.getValue(BeanUsuario.class);
                Singleton.getInstancia().setBeanUsuario(beanUsuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void displaySelectedScreen(int itemId) {

        fragment = null;
        intent = null;

        switch (itemId) {
            case R.id.main_activity_menu_drawer_mapa:
                intent = new Intent(getApplicationContext(), MapsActivityLugares.class);
                break;

            case R.id.main_activity_menu_drawer_publicaciones:
                fragment = new FragmentContenidoPublicaciones();
                break;

            case R.id.main_activity_menu_drawer_lista:
                fragment = new FragmentBusqueda();
                break;

            case R.id.main_activity_menu_drawer_config:
                fragment = new FragmentVacio();
                break;

            case R.id.main_activity_menu_drawer__cerrar:
        fragment = new FragmentNuevoRegistroProblema();
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(frameLayout.getId(), fragment);
            ft.commit();

        } else if (intent != null) {
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
    }
}

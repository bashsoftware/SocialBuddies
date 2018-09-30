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

import bash.socialbuddies.R;
import bash.socialbuddies.fragments.FragmentContenidoPublicaciones;
import bash.socialbuddies.fragments.FragmentVacio;

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

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
                intent = new Intent(getApplicationContext(), MapsActivity.class);
                break;

            case R.id.main_activity_menu_drawer_publicaciones:
                fragment = new FragmentContenidoPublicaciones();
                break;

            case R.id.main_activity_menu_drawer_lista:
                fragment = new FragmentVacio();
                break;

            case R.id.main_activity_menu_drawer_config:
                fragment = new FragmentVacio();
                break;

            case R.id.main_activity_menu_drawer__cerrar:
                fragment = new FragmentVacio();
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

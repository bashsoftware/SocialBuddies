package bash.socialbuddies;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> localizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    localizaciones = new ArrayList<>();
        localizaciones.add(new LatLng(25.542105, -103.407879));
        localizaciones.add(new LatLng(25.534007, -103.434675));
        localizaciones.add(new LatLng(25.529211, -103.415310));
        localizaciones.add(new LatLng(25.515433, -103.413909));
        localizaciones.add(new LatLng(25.517198, -103.389393));
        localizaciones.add(new LatLng(25.537920, -103.395842));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
     for(LatLng marker:localizaciones)
        mMap.addMarker(new MarkerOptions().position(marker).title("Titulo").snippet("Descripcion"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizaciones.get(0),13));
    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getApplicationContext(),"toast"+marker.getPosition(),Toast.LENGTH_SHORT).show();;
            return false;
        }
    });
    }
}

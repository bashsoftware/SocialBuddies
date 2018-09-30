package bash.socialbuddies.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanUbicacion;
import bash.socialbuddies.fragments.FragmentNuevoRegistroProblema;

public class ActivityMapa extends FragmentActivity implements OnMapReadyCallback {

    private FrameLayout frame;
    private GoogleMap mMap;
    private ArrayList<LatLng> localizaciones;
    private ArrayList<MarkerOptions> rangos;
    private FloatingActionButton button;
    private PolylineOptions lineas = null;
    public static ArrayList<BeanUbicacion> puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        lineas = new PolylineOptions();
        mMap = googleMap;
          if(getIntent().getExtras()!=null){

            LatLng latLng = new LatLng(getIntent().getExtras().getDouble("lat"),getIntent().getExtras().getDouble("lng"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            mMap.addMarker(new MarkerOptions().position(latLng));


            }



}
    double measure(double lat1, double lon1, double lat2, double lon2) {  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000; // meters
    }

    void areaMarcadores() {
        mMap.clear();

        PolylineOptions poly = new PolylineOptions();
        for (MarkerOptions m : rangos) {
            mMap.addMarker(m);
            poly.add(m.getPosition());
        }
        poly.color(Color.rgb(3, 169, 244));
        if(rangos.size()>0)
            poly.add(rangos.get(0).getPosition());


        mMap.addPolyline(poly);

    }
    void dibujarLineas(CircleOptions circle,LatLng latLng) {

        PolylineOptions poly = new PolylineOptions();
        for (MarkerOptions m : rangos) {
            poly.add(m.getPosition());
        }
        poly.color(Color.rgb(3, 169, 244));
        poly.add(rangos.get(0).getPosition());
        poly.add(rangos.get(0).getPosition());
        mMap.addCircle(circle);
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.a)));
        mMap.addPolyline(poly);

    }
}



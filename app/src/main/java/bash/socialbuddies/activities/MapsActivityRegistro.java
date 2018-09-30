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

public class MapsActivityRegistro extends FragmentActivity implements OnMapReadyCallback {

    private FrameLayout frame;
    private GoogleMap mMap;
    private ArrayList<LatLng> localizaciones;
    private ArrayList<MarkerOptions> rangos;
    private FloatingActionButton button;
    private PolylineOptions lineas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (FloatingActionButton) findViewById(R.id.activity_maps_float);

        localizaciones = new ArrayList<>();
        rangos = new ArrayList<>();
        localizaciones.add(new LatLng(25.542105, -103.407879));
        localizaciones.add(new LatLng(25.534007, -103.434675));
        localizaciones.add(new LatLng(25.529211, -103.415310));
        localizaciones.add(new LatLng(25.515433, -103.413909));
        localizaciones.add(new LatLng(25.517198, -103.389393));
        localizaciones.add(new LatLng(25.537920, -103.395842));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FragmentNuevoRegistroProblema.latLng != null) {

                    //finalize();
                    double lat = 0, lng = 0;
                    int i = 0;

                    for (MarkerOptions marke : rangos) {
                        i++;
                        lat += marke.getPosition().latitude;
                        lng += marke.getPosition().longitude;

                    }
                    lat = lat / i;
                    lng = lng / i;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_water)).title("final").snippet("snippet final"));

                    int j = 0;
                    float[] f, f1;
                    LatLng mayor = new LatLng(lat, lng);
                    ArrayList<Float> mayores = new ArrayList<>();
                    for (MarkerOptions marke : rangos) {
                        f = new float[1];
                        f1 = new float[1];
                        Location.distanceBetween(lat, lng, marke.getPosition().latitude, marke.getPosition().longitude, f);
                        Location.distanceBetween(lat, lng, mayor.latitude, mayor.longitude, f1);
                        if (f[0] > f1[0]) {
                            mayor = new LatLng(marke.getPosition().latitude, marke.getPosition().longitude);

                        }
                    }

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.strokeColor(R.color.colorPrimary);
                    circleOptions.strokeWidth(3f);
                    circleOptions.radius(measure(lat, lng, mayor.latitude, mayor.longitude));
                    circleOptions.center(new LatLng(lat, lng));
                    circleOptions.fillColor(Color.argb(70, 3, 169, 244));
                    dibujarLineas(circleOptions, new LatLng(lat, lng));
                    FragmentNuevoRegistroProblema.latLng = new BeanUbicacion(lat,lng);
                    ArrayList<BeanUbicacion> puntos = new ArrayList<>();
                    for(MarkerOptions m:rangos){
                        puntos.add(new BeanUbicacion(m.getPosition().latitude,m.getPosition().longitude));
                    }
                    FragmentNuevoRegistroProblema.puntos = puntos;
                    try {
                        this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione Un Logar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (getIntent().getExtras() != null)
            button.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        lineas = new PolylineOptions();
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.544846, -103.406460), 13));



        if (getIntent().getExtras() != null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions  markerOptions = new MarkerOptions();
                    markerOptions.snippet("descripcion");
                    markerOptions.title("Toca Para Borrar");
                    markerOptions.position(latLng);
                    mMap.addMarker(markerOptions);
                    rangos.add(markerOptions);
                    areaMarcadores();
                    FragmentNuevoRegistroProblema.latLng = new BeanUbicacion(latLng.latitude,latLng.longitude);
                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mMap.clear();
                    int i = 0;
                    for(MarkerOptions m:rangos) {

                        if(m.getPosition().equals(marker.getPosition()))
                            break;
                        i++;
                    }
                    rangos.remove(i);
                    areaMarcadores();

                    return false;
                }
            });
        } else {


            // Add a marker in Sydney and move the camera
            for (LatLng marker : localizaciones)
                mMap.addMarker(new MarkerOptions().position(marker).title("Titulo").snippet("Descripcion"));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(getApplicationContext(), "toast" + marker.getPosition(), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
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
        mMap.addCircle(circle);
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_water)));
        mMap.addPolyline(poly);

    }
}
                /*  circleOptions = new CircleOptions();
                    circleOptions.strokeColor(R.color.colorPrimary);
                    circleOptions.strokeWidth(3f);
                    circleOptions.radius(1000);
                    circleOptions.center(latLng);
                    circleOptions.fillColor(Color.argb(70,3,169,244));
                  mMap.addCircle(circleOptions);

*/
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.beans.BeanUbicacion;
import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.fragments.FragmentNuevoRegistroProblema;
import bash.socialbuddies.utilities.FirebaseReference;

public class MapsActivityLugares extends FragmentActivity implements OnMapReadyCallback {

    private FrameLayout frame;
    private GoogleMap mMap;
    private ArrayList<BeanIncidente> incidentes;
    DatabaseReference reference;
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

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.544846, -103.406460), 13));

        reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.INCIDENTES);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                incidentes = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    try{
                        BeanIncidente incidente = snapshot.getValue(BeanIncidente.class);
                    incidente.setInc_id(snapshot.getKey());
                    incidentes.add(incidente);
                }catch (Exception ex){
                        System.out.print(ex);
                }
                }
                setMarkers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
    void setMarkers(){
        lineas = new PolylineOptions();
        for (BeanIncidente incidente : incidentes)
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "toast" + marker.getPosition(), Toast.LENGTH_SHORT).show();

                return false;
            }
        });


ArrayList<ArrayList<LatLng>> circulos = new ArrayList<>();
        for(BeanIncidente incidente:incidentes){

            PolylineOptions poly = new PolylineOptions();
            ArrayList<LatLng> ll = new ArrayList<>();
            for(int i =0;i<incidente.getPuntos().size();i++){
                if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("inundacion")) {
                    poly.color(Color.rgb(3, 169, 244));
                }else if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("socavon")) {
                    poly.color(Color.rgb(201, 147, 94));
                }else{
                    poly.color(Color.rgb(234, 246, 72));
                }

                poly.add(new LatLng(incidente.getPuntos().get(i).getLat(),incidente.getPuntos().get(i).getLng()));

                ll.add(new LatLng(incidente.getPuntos().get(i).getLat(),incidente.getPuntos().get(i).getLng()));
            }
            circulos.add(ll);

            poly.add(new LatLng(incidente.getPuntos().get(0).getLat(),incidente.getPuntos().get(0).getLng()));


            for(LatLng l:ll){
                //finalize();
                double lat = 0, lng = 0;
                int i = 0;

                for (LatLng inc: ll ){
                    i++;
                    lat += inc.latitude;
                    lng += inc.longitude;

                }
                lat = lat / i;
                lng = lng / i;
                if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("inundacion")) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_water)).title(incidente.getInc_titulo()).snippet(incidente.getInc_descripcion()));
                }else if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("socavon")) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_comment)).title(incidente.getInc_titulo()).snippet(incidente.getInc_descripcion()));
                }else{
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(incidente.getInc_titulo()).snippet(incidente.getInc_descripcion()));

                }
                int j = 0;
                float[] f, f1;
                LatLng mayor = new LatLng(lat, lng);
                ArrayList<Float> mayores = new ArrayList<>();
                for (LatLng in:ll) {
                    f = new float[1];
                    f1 = new float[1];
                    Location.distanceBetween(lat, lng, in.latitude, in.longitude, f);
                    Location.distanceBetween(lat, lng, mayor.latitude, mayor.longitude, f1);
                    if (f[0] > f1[0]) {
                        mayor = new LatLng(in.latitude, in.longitude);

                    }
                }

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.strokeColor(R.color.colorPrimary);
                circleOptions.strokeWidth(3f);
                circleOptions.radius(measure(lat, lng, mayor.latitude, mayor.longitude));
                circleOptions.center(new LatLng(lat, lng));
                if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("inundacion")) {
                    circleOptions.fillColor(Color.argb(70, 3, 169, 244));
                }else if(incidente.getMotivo().getTitulo().toString().toLowerCase().equals("socavon")) {
                    circleOptions.fillColor(Color.argb(70, 201, 147, 94));
                }else{
                    circleOptions.fillColor(Color.argb(70, 234, 246, 72));
                }

                mMap.addPolyline(poly);
                mMap.addCircle(circleOptions);



            }




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
}

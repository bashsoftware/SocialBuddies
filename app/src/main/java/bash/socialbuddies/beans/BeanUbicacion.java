package bash.socialbuddies.beans;

public class BeanUbicacion {
    double lat,lng;
    public BeanUbicacion(){}

    public BeanUbicacion(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

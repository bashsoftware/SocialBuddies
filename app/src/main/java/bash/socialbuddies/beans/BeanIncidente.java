package bash.socialbuddies.beans;

import java.util.ArrayList;

public class BeanIncidente {
    private String inc_titulo;
    private String inc_descripcion;
    private String inc_fecha;
    private BeanMotivo motivo;
    private BeanUsuario usuario;
    private ArrayList<String> inc_imgs;

    public String getInc_titulo() {
        return inc_titulo;
    }

    public void setInc_titulo(String inc_titulo) {
        this.inc_titulo = inc_titulo;
    }

    public String getInc_descripcion() {
        return inc_descripcion;
    }

    public void setInc_descripcion(String inc_descripcion) {
        this.inc_descripcion = inc_descripcion;
    }

    public BeanMotivo getMotivo() {
        return motivo;
    }

    public void setMotivo(BeanMotivo motivo) {
        this.motivo = motivo;
    }

    public BeanUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(BeanUsuario usuario) {
        this.usuario = usuario;
    }

    public String getInc_fecha() {
        return inc_fecha;
    }

    public void setInc_fecha(String inc_fecha) {
        this.inc_fecha = inc_fecha;
    }

    public ArrayList<String> getInc_imgs() {
        return inc_imgs;
    }

    public void setInc_imgs(ArrayList<String> inc_imgs) {
        this.inc_imgs = inc_imgs;
    }
}
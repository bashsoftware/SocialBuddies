package bash.socialbuddies.beans;

import java.util.ArrayList;

public class beanIncidente {
    private String inc_id,inc_decripcion;
    private ArrayList<String> inc_imgs;
    private BeanUsuario usuario;
    private BeanMotivo motivo;

    public beanIncidente() {
    }

    public String getInc_id() {
        return inc_id;
    }

    public String getInc_decripcion() {
        return inc_decripcion;
    }

    public ArrayList<String> getInc_imgs() {
        return inc_imgs;
    }

    public BeanUsuario getUsuario() {
        return usuario;
    }

    public BeanMotivo getMotivo() {
        return motivo;
    }

    public void setInc_id(String inc_id) {
        this.inc_id = inc_id;
    }

    public void setInc_decripcion(String inc_decripcion) {
        this.inc_decripcion = inc_decripcion;
    }

    public void setInc_imgs(ArrayList<String> inc_imgs) {
        this.inc_imgs = inc_imgs;
    }

    public void setUsuario(BeanUsuario usuario) {
        this.usuario = usuario;
    }

    public void setMotivo(BeanMotivo motivo) {
        this.motivo = motivo;
    }
}

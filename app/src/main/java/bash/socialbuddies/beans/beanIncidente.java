package bash.socialbuddies.beans;

import java.util.ArrayList;

public class beanIncidente {
    private String id,motivo,decripcion;
    private ArrayList<String> imgs;
    private BeanUsuario usuario;
    private BeanMotivo motivo;

    public beanIncidente() {
    }

    public String getId() {
        return id;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public BeanUsuario getUsuario() {
        return usuario;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public void setUsuario(BeanUsuario usuario) {
        this.usuario = usuario;
    }

    public void setMotivo(BeanMotivo motivo) {
        this.motivo = motivo;
    }
}

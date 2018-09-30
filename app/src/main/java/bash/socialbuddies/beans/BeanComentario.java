package bash.socialbuddies.beans;

public class BeanComentario {

    String com_id;
    String com_contenido;
    Long com_fecha;
    BeanUsuario beanUsuario;

    Boolean meGusta;
    Integer numMeGusta;

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getCom_contenido() {
        return com_contenido;
    }

    public void setCom_contenido(String com_contenido) {
        this.com_contenido = com_contenido;
    }

    public Long getCom_fecha() {
        return com_fecha;
    }

    public void setCom_fecha(Long com_fecha) {
        this.com_fecha = com_fecha;
    }

    public BeanUsuario getBeanUsuario() {
        return beanUsuario;
    }

    public void setBeanUsuario(BeanUsuario beanUsuario) {
        this.beanUsuario = beanUsuario;
    }

    public Boolean getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(Boolean meGusta) {
        this.meGusta = meGusta;
    }

    public Integer getNumMeGusta() {
        return numMeGusta;
    }

    public void setNumMeGusta(Integer numMeGusta) {
        this.numMeGusta = numMeGusta;
    }
}

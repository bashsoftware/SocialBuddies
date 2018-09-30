package bash.socialbuddies.beans;

import java.util.List;

public class BeanPublicacion {

    String idPublicacion;
    Long pub_fecha;
    String pub_descripcion;
    BeanUsuario beanUsuario;
    List<String> beanPublicacionImagenes;

    Boolean meGusta;
    Integer numLikes;
    Integer numComentarios;

    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Long getPub_fecha() {
        return pub_fecha;
    }

    public void setPub_fecha(Long pub_fecha) {
        this.pub_fecha = pub_fecha;
    }

    public String getPub_descripcion() {
        return pub_descripcion;
    }

    public void setPub_descripcion(String pub_descripcion) {
        this.pub_descripcion = pub_descripcion;
    }

    public BeanUsuario getBeanUsuario() {
        return beanUsuario;
    }

    public void setBeanUsuario(BeanUsuario beanUsuario) {
        this.beanUsuario = beanUsuario;
    }

    public List<String> getBeanPublicacionImagenes() {
        return beanPublicacionImagenes;
    }

    public void setBeanPublicacionImagenes(List<String> beanPublicacionImagenes) {
        this.beanPublicacionImagenes = beanPublicacionImagenes;
    }

    public Boolean getMeGusta() {
        return meGusta;
    }

    public void setMeGusta(Boolean meGusta) {
        this.meGusta = meGusta;
    }

    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(Integer numLikes) {
        this.numLikes = numLikes;
    }

    public Integer getNumComentarios() {
        return numComentarios;
    }

    public void setNumComentarios(Integer numComentarios) {
        this.numComentarios = numComentarios;
    }
}

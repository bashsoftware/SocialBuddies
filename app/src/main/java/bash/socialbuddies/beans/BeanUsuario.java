package bash.socialbuddies.beans;

import java.util.HashMap;
import java.util.Map;

public class BeanUsuario {

    private String usu_id;
    private String usu_nombre;
    private String usu_apellido;
    private Integer usu_edad;
    private String usu_correo;
    private String usu_perfil;

    public String getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(String usu_id) {
        this.usu_id = usu_id;
    }

    public String getUsu_nombre() {
        return usu_nombre;
    }

    public void setUsu_nombre(String usu_nombre) {
        this.usu_nombre = usu_nombre;
    }

    public String getUsu_apellido() {
        return usu_apellido;
    }

    public void setUsu_apellido(String usu_apellido) {
        this.usu_apellido = usu_apellido;
    }

    public Integer getUsu_edad() {
        return usu_edad;
    }

    public void setUsu_edad(Integer usu_edad) {
        this.usu_edad = usu_edad;
    }

    public String getUsu_correo() {
        return usu_correo;
    }

    public void setUsu_correo(String usu_correo) {
        this.usu_correo = usu_correo;
    }

    public String getUsu_perfil() {
        return usu_perfil;
    }

    public void setUsu_perfil(String usu_perfil) {
        this.usu_perfil = usu_perfil;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("usu_nombre", usu_nombre);
        result.put("usu_apellido", usu_apellido);
        result.put("usu_correo", usu_correo);
        result.put("usu_edad", usu_edad);
        return result;
    }

}

package bash.socialbuddies.beans;

public class BeanMotivo {
    private String mot_tipo;
    private String mot_titulo;
    private String mot_img;

public BeanMotivo(){}

    public String getMot_tipo() {
        return mot_tipo;
    }

    public String getMot_titulo() {
        return mot_titulo;
    }

    public String getMot_img() {
        return mot_img;
    }

    public void setMot_tipo(String mot_tipo) {
        this.mot_tipo = mot_tipo;
    }

    public void setMot_titulo(String mot_titulo) {
        this.mot_titulo = mot_titulo;
    }

    public void setMot_img(String mot_img) {
        this.mot_img = mot_img;
    }
}

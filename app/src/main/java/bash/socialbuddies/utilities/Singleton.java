package bash.socialbuddies.utilities;

import bash.socialbuddies.beans.BeanUsuario;

public class Singleton {

    private static Singleton instancia;

    private BeanUsuario beanUsuario;

    public static Singleton getInstancia() {
        if (instancia == null) {
            instancia = new Singleton();
        }
        return instancia;
    }

    public BeanUsuario getBeanUsuario() {
        return beanUsuario;
    }

    public void setBeanUsuario(BeanUsuario beanUsuario) {
        this.beanUsuario = beanUsuario;
    }
}

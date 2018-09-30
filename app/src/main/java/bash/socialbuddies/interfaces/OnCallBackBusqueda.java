package bash.socialbuddies.interfaces;

import java.util.ArrayList;

import bash.socialbuddies.beans.BeanIncidente;
import bash.socialbuddies.beans.BeanMotivo;

public interface OnCallBackBusqueda {
    void onGetTipoMotivos(ArrayList<BeanMotivo> arrayMotivos);
    void onGetIncidentes(ArrayList<BeanIncidente> arrayIncidentes);
}

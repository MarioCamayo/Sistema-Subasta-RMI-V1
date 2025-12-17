package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientCallback extends Remote {

    //  Notificación en tiempo real
    void notifyNewBid(String message) throws RemoteException;

    //  Historial de notificaciones
    List<String> getNotifications() throws RemoteException;
}

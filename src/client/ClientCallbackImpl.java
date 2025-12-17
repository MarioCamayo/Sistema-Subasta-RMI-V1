package client;

import common.ClientCallback;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClientCallbackImpl extends UnicastRemoteObject
        implements ClientCallback {

    private final List<String> notifications = new ArrayList<>();

    public ClientCallbackImpl() throws RemoteException {
        super();
    }

    //  Notificación push (tiempo real)
    @Override
    public synchronized void notifyNewBid(String message)
            throws RemoteException {

        notifications.add(message);

        System.out.println("\n NOTIFICACION:");
        System.out.println(message);
        System.out.print("\n ");
    }

    //  Historial de notificaciones
    @Override
    public synchronized List<String> getNotifications()
            throws RemoteException {

        return new ArrayList<>(notifications);
    }
}

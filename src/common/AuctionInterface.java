package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import common.WinnerInfo;

public interface AuctionInterface extends Remote {
    //Métodos remotos que el servidor expone aquí
    //Estos métodos son ejecutados en el servidor cuando el cliente
    //llama a cualquiera de estos métodos Se ejecutan en el servidor, no en el cliente.


    
    WinnerInfo getWinner(int productId) throws RemoteException;

    void registerClient(ClientCallback client) throws RemoteException;

    List<Product> getProducts() throws RemoteException;

    boolean placeBid(int productId, double amount, String user)
            throws RemoteException;

    List<Bid> getBidHistory(int productId)
            throws RemoteException;
}

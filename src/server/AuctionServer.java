package server;

import common.AuctionInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AuctionServer {

    public static void main(String[] args) {
        try {
            AuctionInterface server = new AuctionServerImpl();

            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(1099);
                registry.list(); // prueba si existe
            } catch (Exception e) {
                registry = LocateRegistry.createRegistry(1099);
            }
            // Este es el objeto remoto PRINCIPAL que el servidor expone.
            registry.rebind("AuctionService", server);
            System.out.println(" Servidor RMI de Subastas activo...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

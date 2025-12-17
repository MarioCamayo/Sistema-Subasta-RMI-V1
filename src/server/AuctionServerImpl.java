package server;

import common.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import server.dao.ProductDAO;
import server.dao.BidDAO;
import server.dao.UserDAO;

public class AuctionServerImpl extends UnicastRemoteObject
        implements AuctionInterface {

    private final ProductDAO productDAO;
    private final BidDAO bidDAO;
    private final UserDAO userDAO;

    //  Clientes conectados
    private final List<ClientCallback> clients = new CopyOnWriteArrayList<>();

    // Cierre automático
    private final ScheduledExecutorService scheduler;

    public AuctionServerImpl() throws RemoteException {
        super();

        productDAO = new ProductDAO();
        bidDAO = new BidDAO();
        userDAO = new UserDAO();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(
            () -> productDAO.closeExpiredAuctions(),
            0,
            120,
            TimeUnit.SECONDS
        );

        System.out.println("Servidor RMI de Subastas activo...");
    }

    //  Registro de cliente
    @Override
    public void registerClient(ClientCallback client)
            throws RemoteException {

        clients.add(client);
        System.out.println("Cliente registrado");
    }

    //  Listar productos
    @Override
    public List<Product> getProducts()
            throws RemoteException {

        return productDAO.getAllProducts();
    }

    //  Realizar puja
    @Override
    public boolean placeBid(int productId, double amount, String username)
            throws RemoteException {

        if (amount <= 0) return false;

        int userId = userDAO.getOrCreateUser(username);
        if (userId == -1) return false;

        Product product = productDAO.getProductById(productId);
        if (product == null || !product.isOpen()) return false;

        if (amount <= product.getCurrentBid()) return false;

        // Guardar puja
        bidDAO.saveBid(productId, userId, amount);
        productDAO.updateBid(productId, amount);

        //  Notificar a todos los clientes
        String message =
            " Nueva puja\n" +
            "Producto: " + product.getName() + "\n" +
            "Usuario: " + username + "\n" +
            "Monto: $" + amount;

        for (ClientCallback c : clients) {
            try {
                c.notifyNewBid(message);
            } catch (RemoteException e) {
                clients.remove(c);
            }
        }

        return true;
    }

    //  Historial de pujas
    @Override
    public List<Bid> getBidHistory(int productId)
            throws RemoteException {

        return bidDAO.getBidsByProduct(productId);
    }

    //  Ganador
    @Override
    public WinnerInfo getWinner(int productId)
            throws RemoteException {

        return bidDAO.getWinnerByProduct(productId);
    }
}

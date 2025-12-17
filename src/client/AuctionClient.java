package client;

import common.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class AuctionClient {

    private static Map<String, List<String>> userHistory = new HashMap<>();
    private static Map<String, UserStats> userStats = new HashMap<>();

    public static void main(String[] args) {
        try {
            // CLIENTE consume el objeto remoto
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AuctionInterface server =
                    (AuctionInterface) registry.lookup("AuctionService");

            ClientCallback callback = new ClientCallbackImpl();
            server.registerClient(callback);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("\n=== MENU SUBASTAS ===");
                System.out.println("1. Ver subastas");
                System.out.println("2. Hacer puja");
                System.out.println("3. Ver mi historial de participacion");
                System.out.println("4. Ver mis estadisticas de pujas");
                System.out.println("5. Ver ganador de subasta");
                System.out.println("6. Ver notificaciones");
                System.out.println("0. Salir");
                System.out.print("Opcion: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Opcion invalida.");
                    sc.nextLine();
                    continue;
                }

                int option = sc.nextInt();
                sc.nextLine();

                if (option == 0) {
                    System.out.println("Saliendo...");
                    break;
                }

                switch (option) {

                    case 1:
                        System.out.println("\n=== SUBASTAS ===");
                        List<Product> products = server.getProducts();
                        for (Product p : products) {
                            System.out.println(
                                p.getId() + ". " +
                                p.getName() +
                                " | Precio base: $" + p.getInitialPrice() +
                                " | " + p.getEstado()
                            );
                        }
                        break;

                    case 2:
                        System.out.print("\nID Producto: ");
                        if (!sc.hasNextInt()) {
                            System.out.println(" El ID debe ser numerico.");
                            sc.nextLine();
                            break;
                        }
                        int id = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Usuario: ");
                        String user = sc.nextLine().trim();

                        if (user.isEmpty()) {
                            System.out.println(" El usuario no puede estar vacio.");
                            break;
                        }

                        System.out.print("Monto: ");
                        if (!sc.hasNextDouble()) {
                            System.out.println(" El monto debe ser un numero.");
                            sc.nextLine();
                            break;
                        }

                        double amount = sc.nextDouble();
                        sc.nextLine();

                        if (amount <= 0) {
                            System.out.println(" El monto debe ser mayor que 0.");
                            break;
                        }

                        boolean ok = server.placeBid(id, amount, user);

                        if (ok) {
                            System.out.println("Puja aceptada");

                            userHistory.putIfAbsent(user, new ArrayList<>());
                            userHistory.get(user)
                                    .add("Producto " + id + " → $" + amount);

                            userStats.putIfAbsent(user, new UserStats());
                            userStats.get(user).registrarPuja(amount);

                        } else {
                            System.out.println(" Puja rechazada (subasta cerrada o monto insuficiente).");
                        }
                        break;

                    case 3:
                        System.out.print("\nUsuario: ");
                        String u = sc.nextLine();

                        System.out.println("\n=== HISTORIAL DE " + u + " ===");
                        List<String> history = userHistory.get(u);

                        if (history == null || history.isEmpty()) {
                            System.out.println("No tienes participaciones aun.");
                        } else {
                            history.forEach(h -> System.out.println("- " + h));
                        }
                        break;

                    case 4:
                        System.out.print("\nUsuario: ");
                        String usr = sc.nextLine();

                        System.out.println("\n=== ESTADISTICAS DE " + usr + " ===");
                        UserStats stats = userStats.get(usr);

                        if (stats == null) {
                            System.out.println("No tienes estadisticas aun.");
                        } else {
                            System.out.println("Total de pujas: " + stats.getTotalPujas());
                            System.out.println("Monto total pujado: $" + stats.getTotalMonto());
                            System.out.println("Mayor puja: $" + stats.getPujaMaxima());
                        }
                        break;

                    case 5:
                        System.out.print("\nID Producto: ");
                        if (!sc.hasNextInt()) {
                            System.out.println("ID invalido");
                            sc.nextLine();
                            break;
                        }

                        int pid = sc.nextInt();
                        sc.nextLine();

                        WinnerInfo winner = server.getWinner(pid);

                        if (winner == null) {
                            System.out.println(" No hay ganador aun (subasta activa o sin pujas).");
                        } else {
                            System.out.println("\n GANADOR DE LA SUBASTA ");
                            System.out.println("Producto: " + winner.getProductName());
                            System.out.println("Usuario : " + winner.getUsername());
                            System.out.println("Monto   : $" + winner.getAmount());
                        }
                        break;

                    case 6:
                        List<String> notifications =
                                ((ClientCallbackImpl) callback).getNotifications();

                        System.out.println("\n=== NOTIFICACIONES ===");

                        if (notifications.isEmpty()) {
                            System.out.println("No hay notificaciones aun.");
                        } else {
                            notifications.forEach(n -> System.out.println("- " + n));
                        }
                        break;

                    default:
                        System.out.println(" Opcion invalida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

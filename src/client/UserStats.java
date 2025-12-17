package client;

public class UserStats {
    private int totalPujas = 0;
    private double totalMonto = 0;
    private double pujaMaxima = 0;

    // Registrar una nueva puja del usuario
    public void registrarPuja(double monto) {
        totalPujas++;
        totalMonto += monto;
        if (monto > pujaMaxima) {
            pujaMaxima = monto;
        }
    }

    // Getters para mostrar estadísticas
    public int getTotalPujas() {
        return totalPujas;
    }

    public double getTotalMonto() {
        return totalMonto;
    }

    public double getPujaMaxima() {
        return pujaMaxima;
    }
}

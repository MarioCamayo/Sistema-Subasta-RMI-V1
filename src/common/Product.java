package common;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    

    //  Precio con el que inicia la subasta (NO cambia)
    private double initialPrice;

    //  Puja actual (SOLO para lógica interna del servidor)
    private double currentBid;

    //  ACTIVA o CERRADA
    private String estado;

    //  Constructor completo (alineado con la BD)
    public Product(int id, String name, double initialPrice, double currentBid, String estado) {
        this.id = id;
        this.name = name;
        this.initialPrice = initialPrice;
        this.currentBid = currentBid;
        this.estado = estado;
    }

    // ===================== GETTERS =====================

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ESTE es el precio que se debe mostrar en el menú
    public double getInitialPrice() {
        return initialPrice;
    }

    //  SOLO para uso interno (NO mostrar en menú)
    public double getCurrentBid() {
        return currentBid;
    }

    public String getEstado() {
        return estado;
    }

    // ===================== LÓGICA =====================

    public boolean isOpen() {
        return "ACTIVA".equalsIgnoreCase(estado);
    }
}

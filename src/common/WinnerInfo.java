package common;

import java.io.Serializable;

public class WinnerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productId;
    private String productName;
    private String username;
    private double amount;

    public WinnerInfo(int productId, String productName,
                      String username, double amount) {
        this.productId = productId;
        this.productName = productName;
        this.username = username;
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }
}

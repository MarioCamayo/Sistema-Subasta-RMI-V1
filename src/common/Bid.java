package common;

import java.io.Serializable;

public class Bid implements Serializable {

    private String user;
    private double amount;
    private long timestamp;

    public Bid(String user, double amount) {
        this.user = user;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

package Database.mainClasses;

import java.util.Date;

public class Transaction {
    
    private int id;
    private double amount;
    private Date date;
    private String mer_name;
    private String type;

    public int getId() {

        return id;
    }

    public void setId(int val) {

        id = val;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMer_name() {
        return mer_name;
    }

    public void setMer_name(String mer_name) {
        this.mer_name = mer_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

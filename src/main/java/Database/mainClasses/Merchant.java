package Database.mainClasses;

public class Merchant extends Account{
    protected double commission;
    protected double profit;
    protected double amount_due;

    public Merchant(String name, String username, String password) {
        super(name, username, password);
    }

    public void initfields(int commission){
        this.commission = commission;
        profit = 0;
        amount_due = 0;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public double getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(float amount_due) {
        this.amount_due = amount_due;
    }
}

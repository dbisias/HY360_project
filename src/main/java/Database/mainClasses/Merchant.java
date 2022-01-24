package Database.mainClasses;

public class Merchant extends Account{
    protected float comission;
    protected float profit;
    protected float amount_due;

    public void initfields(int comission){
        this.comission = comission;
        profit = 0;
        amount_due = 0;
    }

    public float getComission() {
        return comission;
    }

    public void setComission(float comission) {
        this.comission = comission;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(float amount_due) {
        this.amount_due = amount_due;
    }
}

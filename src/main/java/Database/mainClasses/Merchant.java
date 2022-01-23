package Database.mainClasses;

public class Merchant extends Account{
    protected float comission;
    protected float profit;
    protected float ammount_due;

    public void initfields(int comission){
        this.comission = comission;
        profit = 0;
        ammount_due = 0;
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

    public float getAmmount_due() {
        return ammount_due;
    }

    public void setAmmount_due(float ammount_due) {
        this.ammount_due = ammount_due;
    }
}

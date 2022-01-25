package Database.mainClasses;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class Company extends Account{
    protected double billimit;
    protected Date expiration_date;
    protected double amount_due;
    protected double remaining_amount;

    public Company(String name, String username, String password) {
        super(name, username, password);
    }

    public void initfields(){
        billimit = 100;
        expiration_date = new Date();
        expiration_date.setYear(expiration_date.getYear()+5);
        amount_due = 0;
        remaining_amount = 300;
    }

    public double getBillimit() {
        return billimit;
    }

    public void setBillimit(double billimit) {
        this.billimit = billimit;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public double getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(double amount_due) {
        this.amount_due = amount_due;
    }

    public double getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(double remaining_amount) {
        this.remaining_amount = remaining_amount;
    }
}
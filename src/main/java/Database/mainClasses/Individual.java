package Database.mainClasses;

import java.util.Date;

public class Individual extends Account{
    protected int company_account_id;
    protected int billimit;
    protected Date expiration_date;
    protected int amount_due;
    protected int remaining_amount;

    public Individual(String name, String username, String password) {
        super(name, username, password);
    }

    public void initfields(){
        billimit = 100;
        expiration_date = new Date();
        expiration_date.setYear(expiration_date.getYear()+5);
        amount_due = 0;
        remaining_amount = 100;
    }

    public int getCompany_account_id() {
        return company_account_id;
    }

    public void setCompany_account_id(int company_account_id) {
        this.company_account_id = company_account_id;
    }

    public int getBillimit() {
        return billimit;
    }

    public void setBillimit(int billimit) {
        this.billimit = billimit;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public int getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(int amount_due) {
        this.amount_due = amount_due;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }
}

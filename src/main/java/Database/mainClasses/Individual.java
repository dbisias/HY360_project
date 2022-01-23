package Database.mainClasses;

import java.util.Date;

public class Individual extends Account{
    protected int company_id;
    protected int billimit;
    protected Date expiration_date;
    protected int ammount_due;
    protected int remaining_amount;

    public void initfields(){
        billimit = 100;
        expiration_date = new Date();
        expiration_date.setYear(expiration_date.getYear()+5);
        ammount_due = 0;
        remaining_amount = 100;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
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

    public int getAmmount_due() {
        return ammount_due;
    }

    public void setAmmount_due(int ammount_due) {
        this.ammount_due = ammount_due;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }
}

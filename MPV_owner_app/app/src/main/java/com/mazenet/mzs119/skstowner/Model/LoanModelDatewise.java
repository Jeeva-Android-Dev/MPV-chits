package com.mazenet.mzs119.skstowner.Model;

/**
 * Created by MZS119 on 5/2/2018.
 */

public class LoanModelDatewise {
    String cust_name;
    String Date;
    String amount;

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }

    String paymode;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReference_Grp() {
        return Reference_Grp;
    }

    public void setReference_Grp(String reference_Grp) {
        Reference_Grp = reference_Grp;
    }

    String Reference_Grp;
}

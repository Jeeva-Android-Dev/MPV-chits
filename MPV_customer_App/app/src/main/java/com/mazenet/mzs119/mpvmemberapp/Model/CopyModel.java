package com.mazenet.mzs119.mpvmemberapp.Model;

import java.util.Date;

public class CopyModel {
    String amount;
    String paytype;
    String advance;
    String date;
    Date dateitme;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public Date getDateitme() {
        return dateitme;
    }

    public void setDateitme(Date dateitme) {
        this.dateitme = dateitme;
    }
    String time;
    String grpname;
    String grpticketno;
    String chit_ticket_type;

    public String getDue_advance() {
        return due_advance;
    }

    public void setDue_advance(String due_advance) {
        this.due_advance = due_advance;
    }

    String due_advance;

    public String getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(String receipt_no) {
        this.receipt_no = receipt_no;
    }

    String receipt_no;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGrpname() {
        return grpname;
    }

    public void setGrpname(String grpname) {
        this.grpname = grpname;
    }

    public String getGrpticketno() {
        return grpticketno;
    }

    public void setGrpticketno(String grpticketno) {
        this.grpticketno = grpticketno;
    }

    public String getChit_ticket_type() {
        return chit_ticket_type;
    }

    public void setChit_ticket_type(String chit_ticket_type) {
        this.chit_ticket_type = chit_ticket_type;
    }
}

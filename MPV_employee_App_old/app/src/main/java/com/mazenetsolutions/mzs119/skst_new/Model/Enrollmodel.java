package com.mazenetsolutions.mzs119.skst_new.Model;

/**
 * Created by admin1 on 12/6/2017.
 */

public class Enrollmodel {

    String Scheme;

    public String getGrpid() {
        return grpid;
    }

    public void setGrpid(String grpid) {
        this.grpid = grpid;
    }

    String grpid;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    String cusid;
    String tableid;
    String enrollid;
    String Pending_Amt;
    String Paid_Amt;
    String Penalty_Amt;
    String Bonus_Amt;
    String Group_Name;
    String payamount;
    String Advanceamnt;
    String upcommimg_installment_no;
    String upcommimg_due_date;
    String upcoming_dueamount;
    String advanceAvail;

    public String getAdvanceAvail() { return  advanceAvail; }
    public String getAdvanceamnt() {
        return Advanceamnt;
    }

    public void setAdvanceAvail(String advanceAvail1){ advanceAvail = advanceAvail1;}

    public void setAdvanceamnt(String advanceamnt) {
        Advanceamnt = advanceamnt;
    }

    String Group_Ticket_Name;
    String cusbranch;

    public String getInsamt() {
        return insamt;
    }

    public void setInsamt(String insamt) {
        this.insamt = insamt;
    }

    String insamt;

    public String getCusbranch() {
        return cusbranch;
    }

    public void setCusbranch(String cusbranch) {
        this.cusbranch = cusbranch;
    }

    public String getPendingdys() {
        return pendingdys;
    }

    public void setPendingdys(String pendingdys) {
        this.pendingdys = pendingdys;
    }

    String pendingdys;

    public String getEnrollid() {
        return enrollid;
    }

    public void setEnrollid(String enrollid) {
        this.enrollid = enrollid;
    }

    public String getUpcommimg_installment_no() {
        return upcommimg_installment_no;
    }

    public void setUpcommimg_installment_no(String upcommimg_installment_no) {
        this.upcommimg_installment_no = upcommimg_installment_no;
    }

    public String getUpcommimg_due_date() {
        return upcommimg_due_date;
    }

    public void setUpcommimg_due_date(String upcommimg_due_date) {
        this.upcommimg_due_date = upcommimg_due_date;
    }

    public String getUpcoming_dueamount() {
        return upcoming_dueamount;
    }

    public void setUpcoming_dueamount(String upcoming_dueamount) {
        this.upcoming_dueamount = upcoming_dueamount;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }


    public String getScheme() {
        return Scheme;
    }

    public void setScheme(String scheme) {
        Scheme = scheme;
    }

    public String getPending_Amt() {
        return Pending_Amt;
    }

    public void setPending_Amt(String pending_Amt) {
        Pending_Amt = pending_Amt;
    }

    public String getPaid_Amt() {
        return Paid_Amt;
    }

    public void setPaid_Amt(String paid_Amt) {
        Paid_Amt = paid_Amt;
    }

    public String getPenalty_Amt() {
        return Penalty_Amt;
    }

    public void setPenalty_Amt(String penalty_Amt) {
        Penalty_Amt = penalty_Amt;
    }

    public String getBonus_Amt() {
        return Bonus_Amt;
    }

    public void setBonus_Amt(String bonus_Amt) {
        Bonus_Amt = bonus_Amt;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public String getGroup_Ticket_Name() {
        return Group_Ticket_Name;
    }

    public void setGroup_Ticket_Name(String group_Ticket_Name) {
        Group_Ticket_Name = group_Ticket_Name;
    }



}
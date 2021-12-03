package com.mazenetsolutions.mzs119.skst_new.Model;

/**
 * Created by admin1 on 12/6/2017.
 */

public class Custmodel {


    String Cusid;
    String customer_id;
    String NAME;
    String MOBILE;
    String advanceamt;
    String pendingamt;
    String totalenrlpending;
    String enrlpaid;
    String level;
    String bonusamt;
    String penaltyamt;
    String pendingdays;

    public String getTomorowdate() {
        return tomorowdate;
    }

    public void setTomorowdate(String tomorowdate) {
        this.tomorowdate = tomorowdate;
    }

    String tomorowdate;

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    String lastdate  ;

    public String getCusid() {
        return Cusid;
    }

    public void setCusid(String cusid) {
        Cusid = cusid;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getAdvanceamt() {
        return advanceamt;
    }

    public void setAdvanceamt(String advanceamt) {
        this.advanceamt = advanceamt;
    }

    public String getPendingamt() {
        return pendingamt;
    }

    public void setPendingamt(String pendingamt) {
        this.pendingamt = pendingamt;
    }

    public String getTotalenrlpending() {
        return totalenrlpending;
    }

    public void setTotalenrlpending(String totalenrlpending) {
        this.totalenrlpending = totalenrlpending;
    }

    public String getEnrlpaid() {
        return enrlpaid;
    }

    public void setEnrlpaid(String enrlpaid) {
        this.enrlpaid = enrlpaid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBonusamt() {
        return bonusamt;
    }

    public void setBonusamt(String bonusamt) {
        this.bonusamt = bonusamt;
    }

    public String getPenaltyamt() {
        return penaltyamt;
    }

    public void setPenaltyamt(String penaltyamt) {
        this.penaltyamt = penaltyamt;
    }

    public String getPendingdays() {
        return pendingdays;
    }

    public void setPendingdays(String pendingdays) {
        this.pendingdays = pendingdays;
    }
}

package com.mazenet.mzs119.skstowner.Model;

/**
 * Created by MZS119 on 3/29/2018.
 */

public class OverallModel {
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTot_amnt() {
        return Tot_amnt;
    }

    public void setTot_amnt(String tot_amnt) {
        Tot_amnt = tot_amnt;
    }

    String Tot_amnt;

    public String getEmp_id() {
        return Emp_id;
    }

    public void setEmp_id(String emp_id) {
        Emp_id = emp_id;
    }

    String Emp_id;

}

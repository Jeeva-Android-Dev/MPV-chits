package com.mazenet.mzs119.skstowner.Model;

/**
 * Created by MZS119 on 4/18/2018.
 */

public class DeviceApprovalModel {
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String Name;
    String id;
    String regid;

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;
}

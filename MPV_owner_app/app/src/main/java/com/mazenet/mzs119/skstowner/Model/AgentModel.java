package com.mazenet.mzs119.skstowner.Model;

/**
 * Created by MZS119 on 3/31/2018.
 */

public class AgentModel {
    String Agentname;

    public String getTotalcollected() {
        return totalcollected;
    }

    public void setTotalcollected(String totalcollected) {
        this.totalcollected = totalcollected;
    }

    String totalcollected;

    public String getCashinhand() {
        return cashinhand;
    }

    public void setCashinhand(String cashinhand) {
        this.cashinhand = cashinhand;
    }

    String cashinhand;

    public String getAgentname() {
        return Agentname;
    }

    public void setAgentname(String agentname) {
        Agentname = agentname;
    }

    public String getAgentId() {
        return AgentId;
    }

    public void setAgentId(String agentId) {
        AgentId = agentId;
    }

    String AgentId;
}

package org.lordalex.thebridgelcp.Utils;

import java.util.List;

public class ConfigTeam {
    private String names;
    private String color;
    private int wool;
    private String spawn;
    private List<String> portal;
    private int yaw;

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWool() {
        return wool;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public String getSpawn() {
        return spawn;
    }

    public void setSpawn(String spawn) {
        this.spawn = spawn;
    }

    public List<String> getPortal() {
        return portal;
    }

    public void setPortal(List<String> bed) {
        this.portal = bed;
    }
}

package org.lordalex.thebridgelcp;

import org.bukkit.ChatColor;
import org.lordalex.thebridgelcp.Utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;

public class TBTeam {
    private String id;
    private String names;
    private String color;
    private int wool;
    private String spawn;
    private List<String> portal;
    private ArrayList<PlayerInfo> players = new ArrayList<>();
    private String activePoints;
    private String inactivePoints;

    public TBTeam(String id, String names, String color, int wool, String spawn, List<String> portal) {
        this.id = id;
        this.names = names;
        this.color = color;
        this.wool = wool;
        this.spawn = spawn;
        this.portal = portal;
        activePoints = ColorUtil.getMessage(ChatColor.getByChar(color) + "&l" + names.charAt(0) + " ");
        inactivePoints = ColorUtil.getMessage("&7⬤⬤⬤⬤⬤");
    }

    public String getActivePoints() {
        return activePoints;
    }

    public void setActivePoints(String activePoints) {
        this.activePoints = activePoints;
    }

    public String getInactivePoints() {
        return inactivePoints;
    }

    public void setInactivePoints(String inactivePoints) {
        this.inactivePoints = inactivePoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setPortal(List<String> portal) {
        this.portal = portal;
    }

    public ArrayList<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerInfo> players) {
        this.players = players;
    }
}

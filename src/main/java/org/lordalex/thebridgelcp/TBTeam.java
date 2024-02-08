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
    private int points;

    public TBTeam(String id, String names, String color, int wool, String spawn, List<String> portal) {
        this.id = id;
        this.names = names;
        this.color = color;
        this.wool = wool;
        this.spawn = spawn;
        this.portal = portal;
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

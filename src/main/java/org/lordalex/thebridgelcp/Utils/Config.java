package org.lordalex.thebridgelcp.Utils;

import java.util.HashMap;

public class Config {
    private String name;
    private String world;
    private String lobby;
    private int teamPlayers;
    private int playersToStart;
    private HashMap<String, ConfigTeam> teams;

    public int getPlayersToStart() {
        return playersToStart;
    }

    public void setPlayersToStart(int playersToStart) {
        this.playersToStart = playersToStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getLobby() {
        return lobby;
    }

    public void setLobby(String lobby) {
        this.lobby = lobby;
    }

    public int getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(int teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public HashMap<String, ConfigTeam> getTeams() {
        return teams;
    }

    public void setTeams(HashMap<String, ConfigTeam> teams) {
        this.teams = teams;
    }
}

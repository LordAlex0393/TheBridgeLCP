package org.lordalex.thebridgelcp;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerInfo {
    //public static final Map<String, PlayerInfo> PLAYERS = new ConcurrentHashMap<>();
    public final Player player;
    public TBTeam team;
    public int kills = 0;
    public int deaths = 0;
    public int points = 0;

    public PlayerInfo(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public TBTeam getTeam() {
        return team;
    }

    public void setTeam(TBTeam team) {
        this.team = team;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

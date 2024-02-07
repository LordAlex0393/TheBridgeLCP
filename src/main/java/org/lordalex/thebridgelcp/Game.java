package org.lordalex.thebridgelcp;

import org.lordalex.thebridgelcp.Utils.GameState;
import org.lordalex.thebridgelcp.Utils.CustomScoreboard;

public class Game {
    private final TheBridgeLCP plugin;
    private GameState state;
    private CustomScoreboard scoreboard;


    public Game(TheBridgeLCP plugin, GameState state) {
        this.plugin = plugin;
        this.state = state;
    }

    public TheBridgeLCP getPlugin() {
        return plugin;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public CustomScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(CustomScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

}

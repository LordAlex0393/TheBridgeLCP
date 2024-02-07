package org.lordalex.thebridgelcp.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashSet;

public class CustomScoreboard {
    public static Scoreboard createGamingScoreboard(ArrayList<String> scores) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("The Bridge", "Test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int i = 3 + scores.size();
        for(String str : scores){
            objective.getScore(str).setScore(i);
            i--;
        }
        objective.getScore(" ").setScore(2);
        objective.getScore(ColorUtil.getMessage("&a&lVimeWorld.ru")).setScore(1);

        return scoreboard;
    }
}

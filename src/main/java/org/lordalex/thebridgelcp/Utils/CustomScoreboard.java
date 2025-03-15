package org.lordalex.thebridgelcp.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;

public class CustomScoreboard {
    public static Scoreboard createScoreboard(ArrayList<String> scores) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(ColorUtil.get("&b&lThe Bridge"), "Test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int i = 2 + scores.size();
        for (String str : scores) {
            objective.getScore(str).setScore(i);
            i--;
        }
        objective.getScore(" ").setScore(2);
        objective.getScore(ColorUtil.get("&a&lneVimeWorld.ru")).setScore(1);

        return scoreboard;
    }
}

package org.lordalex.thebridgelcp.Utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.lordalex.thebridgelcp.PlayerInfo;
import org.lordalex.thebridgelcp.TBTeam;
import org.lordalex.thebridgelcp.TheBridgeLCP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameUtil {
    private static int timer = 10;
    public static void start(){
        TheBridgeLCP.game.setState(GameState.STARTING);

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.getInventory().clear();
        }


       new BukkitRunnable() {
            @Override
            public void run() {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

                Objective objective = scoreboard.registerNewObjective(ColorUtil.getMessage("&b&lThe Bridge"), "Test");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                int online = Bukkit.getOnlinePlayers().size();

                Score s7 = objective.getScore("   ");
                Score s6 = objective.getScore("Карта: " + ChatColor.YELLOW + TheBridgeLCP.config.getName());
                Score s5 = objective.getScore("Игроков: " + ChatColor.YELLOW + online + "/" + TheBridgeLCP.config.getPlayersToStart());
                Score s4 = objective.getScore("  ");
                Score s3 = timer<10? objective.getScore("Начало через: " + ChatColor.YELLOW + "00:0" + timer) : objective.getScore("Начало через: " + ChatColor.YELLOW + "00:" + timer);
                Score s2 = objective.getScore(" ");
                Score s1 = objective.getScore(ColorUtil.getMessage("&a&lVimeWorld.ru"));
                s7.setScore(7);
                s6.setScore(6);
                s5.setScore(5);
                s4.setScore(4);
                s3.setScore(3);
                s2.setScore(2);
                s1.setScore(1);

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.setScoreboard(scoreboard);
                }
                if(TheBridgeLCP.game.getState() != GameState.STARTING){
                    timer = 10;
                    interrupt();
                    cancel();
                }
                if(timer <= 0){
                    timer = 10;
                    game();
                    cancel();
                }
                timer--;
            }
        }.runTaskTimer(TheBridgeLCP.getInstance(), 0, 20);

        if(TheBridgeLCP.game.getState() != GameState.STARTING){
            timer = 10;
        }
    }

    public static void game(){
        TheBridgeLCP.game.setState(GameState.GAME);
        int teamCount = TheBridgeLCP.teams.size();

        int i = 0;
        for(Player p : Bukkit.getOnlinePlayers()){
            PlayerInfo pi = new PlayerInfo(p);
            pi.setTeam(TheBridgeLCP.teams.get(i % teamCount));
            TheBridgeLCP.teams.get(i % teamCount).getPlayers().add(pi);
            p.setPlayerListName(ColorUtil.getMessage("&" + pi.getTeam().getColor() + p.getPlayerListName()));
            p.sendMessage("Your team is " + TheBridgeLCP.teams.get(i % teamCount).getColor());
            i++;


        }

//        Scoreboard scoreboard = CustomScoreboard.createGamingScoreboard(scores);
//        for (Player all : Bukkit.getOnlinePlayers()) {
//            all.setScoreboard(scoreboard);
//        }
    }
    public static void interrupt(){
        TheBridgeLCP.game.setState(GameState.WAITING);

        int online = Bukkit.getOnlinePlayers().size();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(ColorUtil.getMessage("&b&lThe Bridge"), "Test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score s5 = objective.getScore(" ");
        Score s4 = objective.getScore("Карта: " + ChatColor.YELLOW + TheBridgeLCP.config.getName());
        Score s3 = objective.getScore("Игроков: " + ChatColor.YELLOW + online + "/" + TheBridgeLCP.config.getPlayersToStart());
        Score s2 = objective.getScore(" ");
        Score s1 = objective.getScore(ColorUtil.getMessage("&a&lVimeWorld.ru"));
        s5.setScore(5);
        s4.setScore(4);
        s3.setScore(3);
        s2.setScore(2);
        s1.setScore(1);
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(scoreboard);
        }
    }
}

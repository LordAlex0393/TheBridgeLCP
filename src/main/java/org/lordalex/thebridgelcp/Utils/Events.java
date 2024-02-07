package org.lordalex.thebridgelcp.Utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import org.lordalex.thebridgelcp.TheBridgeLCP;
public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        int online = Bukkit.getOnlinePlayers().size();
        e.setJoinMessage(ColorUtil.getMessage("[" + online + "/" + TheBridgeLCP.config.getPlayersToStart() + "] &e=> &fИгрок " + p.getName() + " подключился"));

        Location loc = YmlParser.parseLocation(p.getWorld(), TheBridgeLCP.config.getLobby());
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(loc);

        if (Bukkit.getOnlinePlayers().size() >= TheBridgeLCP.config.getPlayersToStart()) {
            GameUtil.start();
        }
        else if (TheBridgeLCP.game.getState() == GameState.WAITING) {
            ItemStack compassStack = new ItemStack(Material.COMPASS, 1);
            ItemMeta compassMeta = compassStack.getItemMeta();
            compassMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Вернуться в лобби&f <<"));
            compassStack.setItemMeta(compassMeta);
            p.getInventory().setItem(8, compassStack);

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

            Objective objective = scoreboard.registerNewObjective(ColorUtil.getMessage("&b&lThe Bridge"), "Test");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score s5 = objective.getScore("  ");
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
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        int online = Bukkit.getOnlinePlayers().size();
        e.setQuitMessage(ColorUtil.getMessage("[" + (online-1) + "/" + TheBridgeLCP.config.getPlayersToStart() + "] &e<= &fИгрок " + p.getName() + " вышел"));

        if (Bukkit.getOnlinePlayers().size()-1 < TheBridgeLCP.config.getPlayersToStart()) {
            GameUtil.interrupt();
        }

        if (TheBridgeLCP.game.getState() == GameState.WAITING) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();

            Objective objective = scoreboard.registerNewObjective(ColorUtil.getMessage("&b&lThe Bridge"), "Test");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score s5 = objective.getScore(" ");
            Score s4 = objective.getScore("Карта: " + ChatColor.YELLOW + TheBridgeLCP.config.getName());
            Score s3 = objective.getScore("Игроков: " + ChatColor.YELLOW + (online-1) + "/" + TheBridgeLCP.config.getPlayersToStart());
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

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(TheBridgeLCP.game.getState() != GameState.GAME){
            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                Player p = (Player) e.getEntity();
                Location loc = YmlParser.parseLocation(p.getWorld(), TheBridgeLCP.config.getLobby());
                p.teleport(loc);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName() != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
}

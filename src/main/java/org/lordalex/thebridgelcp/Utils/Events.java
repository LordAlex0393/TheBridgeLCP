package org.lordalex.thebridgelcp.Utils;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import org.lordalex.thebridgelcp.Game;
import org.lordalex.thebridgelcp.PlayerInfo;
import org.lordalex.thebridgelcp.TBTeam;
import org.lordalex.thebridgelcp.TheBridgeLCP;
public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setHealth(20);
        p.setFoodLevel(20);
        Location loc = YmlParser.parseLocation(p.getWorld(), TheBridgeLCP.config.getLobby());
        if(TheBridgeLCP.game.getState() == GameState.WAITING){
            int online = Bukkit.getOnlinePlayers().size();
            e.setJoinMessage(ColorUtil.getMessage("[" + online + "/" + TheBridgeLCP.config.getPlayersToStart() + "] &e=> &fИгрок " + p.getName() + " подключился"));

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
        else{
            e.setJoinMessage(ColorUtil.getMessage("&eИгра переполнена. Вы были телепортированы в режиме наблюдателя."));
            p.setGameMode(GameMode.SPECTATOR);
        }
        p.teleport(loc);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        int online = Bukkit.getOnlinePlayers().size();
        e.setQuitMessage(ColorUtil.getMessage("[" + (online-1) + "/" + TheBridgeLCP.config.getPlayersToStart() + "] &e<= &fИгрок " + p.getName() + " вышел"));

        if (TheBridgeLCP.game.getState() == GameState.STARTING && (Bukkit.getOnlinePlayers().size()-1 < 2)) {
            GameUtil.interrupt();
        }
        if(TheBridgeLCP.game.getState() == GameState.GAME){
            e.setQuitMessage(ColorUtil.getMessage("[" + (online-1) + "/" + TheBridgeLCP.config.getPlayersToStart() + "] &e<= &fИгрок &" + TheBridgeLCP.getPlayerInfo(p).getTeam().getColor() + p.getName() + "&f вышел"));
            if(Bukkit.getOnlinePlayers().size()-1 < 2){
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(!(p.equals(player))){
                        GameUtil.finish(TheBridgeLCP.getPlayerInfo(p).getTeam());
                    }
                }
            }
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
        else{
            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
//                Player p = (Player) e.getEntity();
//                PlayerInfo pi = TheBridgeLCP.getPlayerInfo(p);
//                Location loc = YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn());
//                p.teleport(loc);
//                e.setCancelled(true);
                e.setDamage(200000);
            }
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                e.setCancelled(true);
            }
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

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAchievementAwardedEvent(PlayerAchievementAwardedEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if(TheBridgeLCP.game.getState() == GameState.GAME) {
            Player victim = (Player) e.getEntity();
            PlayerInfo victimInfo = TheBridgeLCP.getPlayerInfo(victim);
            if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)) {
                Player damager = (Player) e.getDamager();
                PlayerInfo damagerInfo = TheBridgeLCP.getPlayerInfo(damager);
                if (damagerInfo.getTeam().equals(victimInfo.getTeam())) {
                    e.setCancelled(true);
                }
            }
            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player shooter = (Player) arrow.getShooter();
                    PlayerInfo shooterInfo = TheBridgeLCP.getPlayerInfo(shooter);
                    if (shooterInfo.getTeam().equals(victimInfo.getTeam())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        if(!(e.getBlock().getType().equals(Material.STAINED_CLAY))){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Location blockLoc = e.getBlock().getLocation();
        int radius = GameUtil.PROTECTED_RADIUS;
        for(TBTeam team : TheBridgeLCP.teams){
            Location portal = YmlParser.parseLocation(p.getWorld(), team.getPortal().get(4));
            if((Math.abs(portal.getX() - blockLoc.getX()) <= radius) && (Math.abs(portal.getZ() - blockLoc.getZ()) <= radius)){
                p.sendMessage("X: " + (int) portal.getX() + " / " + (int) blockLoc.getX() + " = " + Math.abs(Math.abs(portal.getX()) - Math.abs(blockLoc.getX())));
                p.sendMessage( "Z: " + (int) portal.getZ() + " / " + (int) blockLoc.getZ() + " = " + Math.abs(Math.abs(portal.getZ()) - Math.abs(blockLoc.getZ())));
                e.setCancelled(true);
                return;
            }
//            else if(Math.abs(Math.abs(portal.getZ()) - Math.abs(blockLoc.getZ())) <= radius){
//                p.sendMessage( "Z: " + (int) portal.getZ() + " / " + (int) blockLoc.getZ() + " = " + Math.abs(Math.abs(portal.getZ()) - Math.abs(blockLoc.getZ())));
//                e.setCancelled(true);
//                return;
//            }
        }
        GameUtil.PLACED_BLOCKS.add(blockLoc);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e){
        if(e == null) return;
        if(e.getEntity() == null) return;
        if(e.getEntity().getPlayer() == null) return;
        if(TheBridgeLCP.game.getState() == GameState.GAME){
            Player victim = e.getEntity().getPlayer();
            PlayerInfo victimInfo = TheBridgeLCP.getPlayerInfo(victim);
            if(e.getEntity().getKiller() instanceof Player){
                Player killer = e.getEntity().getKiller();
                PlayerInfo killerInfo = TheBridgeLCP.getPlayerInfo(killer);
                killerInfo.setKills(killerInfo.getKills()+1);
                for(Player all : Bukkit.getOnlinePlayers()){
                    all.sendMessage(ColorUtil.getMessage("&" + victimInfo.getTeam().getColor() + victim.getName() + "&f был убит игроком &" + killerInfo.getTeam().getColor() + killer.getName()));
                }
                GameUtil.updateGamingScoreboard(killerInfo);
            }
                victim.getInventory().clear();
                victim.spigot().respawn();
        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        if(TheBridgeLCP.game.getState() == GameState.GAME){
            Player p = e.getPlayer();
            PlayerInfo pi = TheBridgeLCP.getPlayerInfo(p);
            //e.setRespawnLocation(YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn()));
            p.teleport(YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn()));
            GameUtil.giveKit(pi);
            e.getPlayer().setBedSpawnLocation(YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn()), true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String msg = e.getMessage();
        e.setCancelled(true);
        if(TheBridgeLCP.game.getState() == GameState.GAME || TheBridgeLCP.game.getState() == GameState.ENDING){
            PlayerInfo pi = TheBridgeLCP.getPlayerInfo(p);
            if (msg.length() > 0 && msg.charAt(0) == '!'){
                for(Player all : Bukkit.getOnlinePlayers()){
                    all.sendMessage(ColorUtil.getMessage("&7(Всем) " + "&" + pi.getTeam().getColor() + p.getName() + "&7: &f" + msg.substring(1)));
                }
            }
            else{
//                e.setMessage(ColorUtil.getMessage("&" + pi.getTeam().getColor() + "(Команда) " + p.getName() + "&7: &f" + msg));
                for(PlayerInfo teamPlayerInfo : pi.getTeam().getPlayers()){
                    teamPlayerInfo.getPlayer().sendMessage(ColorUtil.getMessage("&" + pi.getTeam().getColor() + "(Команда) " + p.getName() + "&7: &f" + msg));
                }
            }
        }
        else{
            for(Player all : Bukkit.getOnlinePlayers()){
                p.sendMessage(ColorUtil.getMessage("&7" + p.getName() + ": &f" + msg));
            }
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent e){
        Player p = e.getPlayer();
        PlayerInfo pi = TheBridgeLCP.getPlayerInfo(p);

        int X1 = (int) p.getLocation().getX();
        int Y1 = (int) p.getLocation().getY();
        int Z1 = (int) p.getLocation().getZ();
        if(TheBridgeLCP.game.getState() == GameState.GAME){
            for(TBTeam team : TheBridgeLCP.teams){
                if(!(team.equals(pi.getTeam()))){
                    for(String portalStr : team.getPortal()){
                        Location loc = YmlParser.parseLocation(p.getWorld(), portalStr);
                        int X2 = (int) loc.getX();
                        int Y2 = (int) loc.getY();
                        int Z2 = (int) loc.getZ();

                        if((X1==X2) && (Y1==Y2) && (Z1==Z2)){
                            pi.getTeam().setPoints(pi.getTeam().getPoints()+1);
                            pi.setPoints(pi.getPoints()+1);
                            for(PlayerInfo pi2 : TheBridgeLCP.players){
                                GameUtil.updateGamingScoreboard(pi2);
                            }
                            if(pi.getTeam().getPoints() == GameUtil.SCORES_TO_WIN){
                                GameUtil.finish(pi.getTeam());
                            }
                            else{
                                GameUtil.restartRound();
                            }
                            for(Player all : Bukkit.getOnlinePlayers()){
                                all.sendMessage(ColorUtil.getMessage("&" + pi.getTeam().getColor() + p.getName() + "&f добрался до портала"));
                            }
                        }
                    }
                }
            }
        }
        e.setCancelled(true);
    }
}

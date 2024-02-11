package org.lordalex.thebridgelcp.Utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.lordalex.thebridgelcp.PlayerInfo;
import org.lordalex.thebridgelcp.TBTeam;
import org.lordalex.thebridgelcp.TheBridgeLCP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GameUtil {
    public static int DELAY = 10;
    public static int SCORES_TO_WIN = 5;
    public static int PROTECTED_RADIUS = 7;
    public static int MAX_BUILD_HEIGHT = 85;
    public static ArrayList<Location> PLACED_BLOCKS = new ArrayList<>();
    private static int timer = DELAY;
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
                    timer = DELAY;
                    interrupt();
                    cancel();
                }
                if(timer <= 0){
                    timer = DELAY;
                    game();
                    cancel();
                }
                timer--;
            }
        }.runTaskTimer(TheBridgeLCP.getInstance(), 0, 20);

        if(TheBridgeLCP.game.getState() != GameState.STARTING){
            timer = DELAY;
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
            TheBridgeLCP.players.add(pi);
            p.setPlayerListName(ColorUtil.getMessage("&" + pi.getTeam().getColor() + p.getPlayerListName()));
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(ColorUtil.getMessage("&fВы играете за &" + pi.getTeam().getColor() + pi.getTeam().getNames().split(", ")[1] + " команду"));
            p.setBedSpawnLocation(YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn()), true);
            i++;
            updateGamingScoreboard(pi);
            restartRound();
        }
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

    public static void finish(TBTeam winner){
        for(Entity current : Bukkit.getServer().getWorld("world").getEntities()) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }

        for(PlayerInfo pi : TheBridgeLCP.players){
            Player p = pi.getPlayer();
            Location locT = YmlParser.parseLocation(p.getWorld(), TheBridgeLCP.config.getLobby());
            p.teleport(locT);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            for(PotionEffect pe : p.getActivePotionEffects()){
                p.removePotionEffect(pe.getType());
            }
            p.setHealth(20);
            p.setFoodLevel(20);
        }
        //String line = "&b&l-----------------------";
        String line = "&b&l-------------------------";
        ArrayList<String> finishStrings = new ArrayList<>();
        finishStrings.add(ColorUtil.getMessage(line));
        finishStrings.add(ColorUtil.getMessage("&e&l   The Bridge"));
        finishStrings.add(ColorUtil.getMessage(" "));
        if(winner == null){
            finishStrings.add(ColorUtil.getMessage(" &f&l НИЧЬЯ"));
        }
        else{
            finishStrings.add(ColorUtil.getMessage(" &" + winner.getColor() + winner.getNames().split(", ")[2] + " победили!"));
        }
        finishStrings.add(ColorUtil.getMessage(""));
        finishStrings.add(ColorUtil.getMessage("&f Лучшие игроки:"));
        int i = 1;
        int score = SCORES_TO_WIN;
        HashSet<PlayerInfo> set = new HashSet<>();
        while((i < 3) && (score > 0)){
            for(PlayerInfo pi : TheBridgeLCP.players){
                if(!(set.contains(pi))){
                    if(pi.getPoints() == score){
                        String pointStr = "очко";
                        if((score >= 2) && (score <= 4)) pointStr = "очка";
                        else if(score > 4) pointStr = "очков";
                        finishStrings.add(ColorUtil.getMessage(" &f" + i + ". &" + pi.getTeam().getColor() + pi.getPlayer().getName() + "&c (" + score + " " + pointStr + ")"));
                        set.add(pi);
                        i++;
                    }
                }
            }
            score--;
        }
        finishStrings.add(ColorUtil.getMessage(line));
        for(Player p : Bukkit.getOnlinePlayers()){
            for(String str : finishStrings){
                p.sendMessage(str);
            }
            ItemStack compassStack = new ItemStack(Material.COMPASS, 1);
            ItemMeta compassMeta = compassStack.getItemMeta();
            compassMeta.setDisplayName(ColorUtil.getMessage("&f >>&e&l Вернуться в лобби&f <<"));
            compassStack.setItemMeta(compassMeta);
            p.getInventory().setItem(8, compassStack);
        }
        TheBridgeLCP.game.setState(GameState.ENDING);

        new BukkitRunnable(){
            @Override
            public void run(){
                for(Player p : Bukkit.getOnlinePlayers()){
                    TheBridgeLCP.teleportToLobby(p);
                }
//                new BukkitRunnable(){
//                    @Override
//                    public void run(){
//                        Bukkit.getServer().shutdown();
//                        try {
////                            Process process = Runtime.getRuntime().exec(
////                                    "cmd /c startTB.bat", null, new File("C:\\Users\\Lord_Alex\\Desktop\\LordWorld\\TheBridge\\"));
//                            Process process = Runtime.getRuntime().exec(
//                                    "cmd /c startTB.bat", null, new File("C:\\Users\\orlov\\MineServer_09_02_2024\\TheBridge\\"));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }.runTaskLater(TheBridgeLCP.getInstance(), 20);
            }
        }.runTaskLater(TheBridgeLCP.getInstance(), 300);

    }
    public static void updateGamingScoreboard(PlayerInfo pi){
        ArrayList<String> scores = new ArrayList<>();
        scores.add("    ");
        for(TBTeam team : TheBridgeLCP.teams){
            String activeScores = new String(new char[team.getPoints()]).replace("\0", "⬤");
            String inactiveScores = new String(new char[SCORES_TO_WIN-team.getPoints()]).replace("\0", "⬤");
            if(pi.getTeam().equals(team)){
                scores.add(ColorUtil.getMessage("&" + team.getColor() + "&l" + team.getNames().charAt(0) + " &" + team.getColor() + activeScores + "&7" + inactiveScores + "&a <= Вы"));
            }
            else{
                scores.add(ColorUtil.getMessage("&" + team.getColor() + "&l" + team.getNames().charAt(0) + " &" + team.getColor() + activeScores + "&7" + inactiveScores));
            }
        }
        scores.add("   ");
        scores.add(ColorUtil.getMessage("&fУбийств:&e " + pi.getKills()));
        scores.add(ColorUtil.getMessage("&fОчков:&e " + pi.getPoints()));
        scores.add("  ");
        scores.add(ColorUtil.getMessage("&fКарта:&e " + TheBridgeLCP.config.getName()));
        Scoreboard scoreboard = CustomScoreboard.createScoreboard(scores);
        pi.getPlayer().setScoreboard(scoreboard);
    }
    public static void updateWaitingScoreboard(Player p, int online){
        ArrayList<String> scores = new ArrayList<>();
        scores.add("  ");
        scores.add("Карта: " + ChatColor.YELLOW + TheBridgeLCP.config.getName());
        scores.add("Игроков: " + ChatColor.YELLOW + online + "/" + TheBridgeLCP.config.getPlayersToStart());
        Scoreboard scoreboard = CustomScoreboard.createScoreboard(scores);
        p.setScoreboard(scoreboard);
    }

    public static void restartRound(){
        for(TBTeam team : TheBridgeLCP.teams){
            Location loc = YmlParser.parseLocation(Bukkit.getServer().getWorld("world"), team.getSpawn());
            List<Location> area = new ArrayList<>();
            int y = -2;
            area.add(loc.clone().add(0, y, 0));
            area.add(loc.clone().add(1, y, 0));
            area.add(loc.clone().add(0, y, 1));
            area.add(loc.clone().add(1, y, 1));
            area.add(loc.clone().add(1, y, -1));
            area.add(loc.clone().add(0, y, -1));
            area.add(loc.clone().add(-1, y, 1));
            area.add(loc.clone().add(-1, y, 0));
            area.add(loc.clone().add(-1, y, -1));

            for(; y <= 1; y++){
                area.add(loc.clone().add(-2, y, -2));
                area.add(loc.clone().add(-2, y, -1));
                area.add(loc.clone().add(-2, y, 0));
                area.add(loc.clone().add(-2, y, 1));
                area.add(loc.clone().add(-2, y, 2));
                area.add(loc.clone().add(2, y, -2));
                area.add(loc.clone().add(2, y, -1));
                area.add(loc.clone().add(2, y, 0));
                area.add(loc.clone().add(2, y, 1));
                area.add(loc.clone().add(2, y, 2));

                area.add(loc.clone().add(-1, y, 2));
                area.add(loc.clone().add(-1, y, -2));
                area.add(loc.clone().add(0, y, 2));
                area.add(loc.clone().add(0, y, -2));
                area.add(loc.clone().add(1, y, 2));
                area.add(loc.clone().add(1, y, -2));
            }

            area.add(loc.clone().add(0, y, 0));
            area.add(loc.clone().add(1, y, 0));
            area.add(loc.clone().add(0, y, 1));
            area.add(loc.clone().add(1, y, 1));
            area.add(loc.clone().add(1, y, -1));
            area.add(loc.clone().add(0, y, -1));
            area.add(loc.clone().add(-1, y, 1));
            area.add(loc.clone().add(-1, y, 0));
            area.add(loc.clone().add(-1, y, -1));

            for(Location l : area){
                ItemStack glassStack = new ItemStack(Material.GLASS, 1, (byte) team.getWool());
                Block block = l.getWorld().getBlockAt(l);
                block.setType(Material.GLASS);
                block.setData((byte) team.getWool());
                //l.getWorld().getBlockAt(l)
            }

            new BukkitRunnable(){
                @Override
                public void run(){
                    for(Location l : area){
                        l.getWorld().getBlockAt(l).setType(Material.AIR);
                    }
                }
            }.runTaskLater(TheBridgeLCP.getInstance(), 100);
        }
        for(PlayerInfo pi : TheBridgeLCP.players){
            Player p = pi.getPlayer();
            p.setHealth(20);
            Location loc = YmlParser.parseLocation(p.getWorld(), pi.getTeam().getSpawn());
            loc.setPitch(p.getLocation().getPitch());
            loc.setYaw(p.getLocation().getYaw());
            p.teleport(loc);
            giveKit(pi);

        }
    }
    public static void giveKit(PlayerInfo pi){
        Player p = pi.getPlayer();
        for(PotionEffect pe : p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
        //p.getInventory().clear();
        ItemStack swordStack = new ItemStack(Material.IRON_SWORD, 1);
        swordStack.setDurability((short) -1);
        ItemStack bowStack = new ItemStack(Material.BOW, 1);
        bowStack.setDurability((short) -1);
        ItemStack pickaxeStack = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta pickaxeMeta = pickaxeStack.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 2, false);
        pickaxeStack.setItemMeta(pickaxeMeta);
        pickaxeStack.setDurability((short) -1);
        ItemStack appleStack = new ItemStack(Material.GOLDEN_APPLE, TheBridgeLCP.players.size()*2);
        ItemStack arrowStack = new ItemStack(Material.ARROW, TheBridgeLCP.players.size()*2);
        ItemStack clayStack = new ItemStack(Material.STAINED_CLAY, 64, (byte) pi.getTeam().getWool());
        if(!(p.getInventory().contains(Material.IRON_SWORD))){
            p.getInventory().setItem(0, swordStack);
            p.getInventory().setItem(1, bowStack);
            p.getInventory().setItem(2, pickaxeStack);
            p.getInventory().setItem(3, clayStack);
            p.getInventory().setItem(4, clayStack);
            p.getInventory().setItem(5, arrowStack);
            p.getInventory().setItem(6, appleStack);
        }
        else{
            int clayCount = 0;
            int arrowCount = 0;
            int appleCount = 0;
            for(ItemStack is : p.getInventory().getContents()){
                if(is == null) continue;
                if(is.getType()==Material.STAINED_CLAY){
                    if(is.getData().getData() == clayStack.getData().getData()){
                        is.setAmount(64);
                        clayCount++;
                    }
                    else{
                        p.getInventory().removeItem(is);
                    }
                }
                else if(is.getType()==Material.ARROW){
                    is.setAmount(TheBridgeLCP.players.size()*2);
                    arrowCount++;
                }
                else if(is.getType()==Material.GOLDEN_APPLE){
                    is.setAmount(TheBridgeLCP.players.size()*2);
                    appleCount++;
                }
            }
            while(clayCount < 2){
                p.getInventory().addItem(clayStack);
                clayCount++;
            }
            while(arrowCount < 1){
                p.getInventory().addItem(arrowStack);
                arrowCount++;
            }
            while(appleCount < 1){
                p.getInventory().addItem(appleStack);
                appleCount++;
            }
        }

        ItemStack bootsStack = new ItemStack(Material.LEATHER_BOOTS, 1, (byte) pi.getTeam().getWool());
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) bootsStack.getItemMeta();
        bootsMeta.setColor(translateChatColorToColor(pi.getTeam().getId()));
        bootsStack.setItemMeta(bootsMeta);
        bootsStack.setDurability((short) -1);
        p.getInventory().setBoots(bootsStack);

        ItemStack leggingsStack = new ItemStack(Material.LEATHER_LEGGINGS, 1, (byte) pi.getTeam().getWool());
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggingsStack.getItemMeta();
        leggingsMeta.setColor(translateChatColorToColor(pi.getTeam().getId()));
        leggingsStack.setItemMeta(leggingsMeta);
        leggingsStack.setDurability((short) -1);
        p.getInventory().setLeggings(leggingsStack);

        ItemStack chestplateStack = new ItemStack(Material.LEATHER_CHESTPLATE, 1, (byte) pi.getTeam().getWool());
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplateStack.getItemMeta();
        chestplateMeta.setColor(translateChatColorToColor(pi.getTeam().getId()));
        chestplateStack.setItemMeta(chestplateMeta);
        chestplateStack.setDurability((short) -1);
        p.getInventory().setChestplate(chestplateStack);
    }
    public static Color translateChatColorToColor(String color)
    {
        switch (color.toUpperCase()) {
            case "AQUA":
                return Color.AQUA;
            case "BLACK":
                return Color.BLACK;
            case "BLUE":
                return Color.BLUE;
            case "DARK_AQUA":
                return Color.BLUE;
            case "DARK_BLUE":
                return Color.BLUE;
            case "DARK_GRAY":
                return Color.GRAY;
            case "DARK_GREEN":
                return Color.GREEN;
            case "DARK_PURPLE":
                return Color.PURPLE;
            case "DARK_RED":
                return Color.RED;
            case "GOLD":
                return Color.YELLOW;
            case "GRAY":
                return Color.GRAY;
            case "GREEN":
                return Color.GREEN;
            case "LIGHT_PURPLE":
                return Color.PURPLE;
            case "RED":
                return Color.RED;
            case "WHITE":
                return Color.WHITE;
            case "YELLOW":
                return Color.YELLOW;
            default:
                break;
        }

        return null;
    }
}

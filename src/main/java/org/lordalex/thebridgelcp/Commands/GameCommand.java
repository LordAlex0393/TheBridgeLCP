package org.lordalex.thebridgelcp.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lordalex.thebridgelcp.PlayerInfo;
import org.lordalex.thebridgelcp.TBTeam;
import org.lordalex.thebridgelcp.TheBridgeLCP;
import org.lordalex.thebridgelcp.Utils.ColorUtil;
import org.lordalex.thebridgelcp.Utils.GameState;
import org.lordalex.thebridgelcp.Utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class GameCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args == null || args.length < 1){
            if(sender instanceof Player){
                Player p = (Player) sender;
                printCommandInfo(p);
                return true;
            }
        }
        if(sender.isOp()){
            if(args[0].equalsIgnoreCase("start")){
                GameUtil.start();
            }
            else if(args[0].equalsIgnoreCase("stop")){
                if(TheBridgeLCP.game.getState() == GameState.STARTING){
                    GameUtil.interrupt();
                }
                else if(TheBridgeLCP.game.getState() == GameState.GAME){
//                    int max1 = Integer.MIN_VALUE;
//                    int max2 = Integer.MIN_VALUE;
//                    for(TBTeam team : TheBridgeLCP.teams){
//                        if(team.getPoints() > max1){
//                            max1 = team.getPoints();
//                        }
//                        else if(team.getPoints() == max2){
//
//                        }
//                    }
                    GameUtil.finish(null);
                }
                else{
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        p.sendMessage(ColorUtil.getMessage("&cИгра ещё не запущена"));
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("flag")){
                if(args.length > 1){
                    if(args[1].equalsIgnoreCase("max-points")){
                        if(args.length > 2){
                            try{
                                int points = Integer.parseInt(args[2]);
                                GameUtil.SCORES_TO_WIN = points;
                                if(TheBridgeLCP.game.getState() == GameState.GAME){
                                    for(PlayerInfo pi : TheBridgeLCP.players){
                                        GameUtil.updateGamingScoreboard(pi);
                                    }
                                }
                            }
                            catch (NumberFormatException ex){
                                if(sender instanceof Player){
                                    Player p = (Player) sender;
                                    p.sendMessage(ColorUtil.getMessage("&Количество очков должно быть числом"));
                                }
                            }
                        }
                    }
                    else if(args[1].equalsIgnoreCase("max-players")){
                        if(args.length > 2){
                            try{
                                int maxPlayers = Integer.parseInt(args[2]);
                                TheBridgeLCP.config.setPlayersToStart(maxPlayers);
                                if(Bukkit.getOnlinePlayers().size() == TheBridgeLCP.config.getPlayersToStart()){
                                    GameUtil.start();
                                }
                                else{
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        GameUtil.updateWaitingScoreboard(all, Bukkit.getOnlinePlayers().size());
                                    }
                                }
                            }
                            catch (NumberFormatException ex){
                                if(sender instanceof Player){
                                    Player p = (Player) sender;
                                    p.sendMessage(ColorUtil.getMessage("&Количество игроков должно быть числом"));
                                }
                            }
                        }
                    }
                }
                else{
                    if(sender instanceof Player){
                        Player p = (Player) sender;
                        printFlagInfo(p);
                        return true;
                    }
                }
            }
            return true;
        }
        return true;
    }
    private static void printCommandInfo(Player p){
        p.sendMessage(ColorUtil.getMessage("&e---------- &dУправление игрой&f (&e/game&f)&e ----------"));
        p.sendMessage(ColorUtil.getMessage("&e/game&7 start&f: запустить игру"));
        p.sendMessage(ColorUtil.getMessage("&e/game&7 stop&f: остановить игру"));
        p.sendMessage(ColorUtil.getMessage("&e/game&7 flag&f: управление настройками сервера"));
        //p.sendMessage(ColorUtil.getMessage("&e/game&7 kick <ник игрока>&f: выкинуть игрока в лобби"));
        //p.sendMessage(ColorUtil.getMessage("&e/game&7 list&f: список игроков на сервере"));
        //p.sendMessage(ColorUtil.getMessage("&e/game&7 sf <ник игрока>&f: принудительно перекинуть игрока на сервер"));
    }
    private static void printFlagInfo(Player p){
        p.sendMessage(ColorUtil.getMessage("&e---------- &dНастройки сервера&f (&e/game flag&f)&e ----------"));
        p.sendMessage(ColorUtil.getMessage("&dmax-points&f (&7Количество очков необходимых для победы&f): " + GameUtil.SCORES_TO_WIN));
        p.sendMessage(ColorUtil.getMessage("&dmax-players&f (&7Количество игроков необходимых для старта игры&f): " + TheBridgeLCP.config.getPlayersToStart()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            List<String> list = new ArrayList<>();
            if(args[0].equalsIgnoreCase("flag")){
                list.add("max-points");
                list.add("max-players");
            }
            else if(args[0].isEmpty()){
                list.add("start");
                list.add("stop");
                list.add("flag");
            }
            return list;
        }
        return null;
    }
}


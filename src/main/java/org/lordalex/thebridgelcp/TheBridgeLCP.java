package org.lordalex.thebridgelcp;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;
import org.lordalex.thebridgelcp.Utils.*;

import java.io.File;
import java.util.*;

public final class TheBridgeLCP extends JavaPlugin implements PluginMessageListener, Listener {
    private static Plugin instance;
    public static Config config;
    public static Game game;
    public static ArrayList<TBTeam> teams = new ArrayList<>();
    public static HashSet<PlayerInfo> players = new HashSet<>();
    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        game = new Game(this, GameState.WAITING);

        File file = new File("plugins\\TheBridgeLCP\\config.yml");
        config = YmlParser.parseMapConfig(file);


        for(String configId : config.getTeams().keySet()){
            teams.add(new TBTeam(configId, config.getTeams().get(configId).getNames(), config.getTeams().get(configId).getColor(), config.getTeams().get(configId).getWool(), config.getTeams().get(configId).getSpawn(), config.getTeams().get(configId).getPortal()));
        }


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(World world : Bukkit.getServer().getWorlds()) {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setTime(3000);
                }
            }
        }, 0L, 20L);

    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public static Plugin getInstance(){
        return instance;
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if (e == null) return;
        Player p = e.getPlayer();
        if (e.getItem() == null) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem().getType() == Material.COMPASS) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("lobby");
            p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
            p.sendMessage(ColorUtil.getMessage("&aВы были перемещены в лобби"));
        }
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }
}

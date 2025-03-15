package org.lordalex.thebridgelcp;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.lordalex.thebridgelcp.Commands.GameCommand;
import org.lordalex.thebridgelcp.Utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public final class TheBridgeLCP extends JavaPlugin implements PluginMessageListener, Listener {
    public static HashSet<PlayerInfo> players = new HashSet<>();
    public static ArrayList<TBTeam> teams = new ArrayList<>();
    private static Plugin instance;
    public static Config config;
    public static Game game;

    @Override
    public void onEnable() {
        instance = this;
        registerAllEvents();
        registerAllCommands();
        game = new Game(this, GameState.WAITING);
        File file = new File("config.yml");
        config = YmlParser.parseMapConfig(file);
        initGame();
    }

    private void initGame() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setAutoSave(false);
        }
        for (Map.Entry<String, ConfigTeam> entry : config.getTeams().entrySet()) {
            ConfigTeam configTeam = entry.getValue();
            String configId = entry.getKey();
            teams.add(new TBTeam(
                    configId,
                    configTeam.getNames(),
                    configTeam.getColor(),
                    configTeam.getWool(),
                    configTeam.getSpawn(),
                    configTeam.getPortal()
            ));
        }
        GameUtil.MAX_BUILD_HEIGHT = Integer.parseInt(teams.get(0).getPortal().split(", ")[1]) + 15;
    }

    private void registerAllEvents() {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void registerAllCommands() {
        getCommand("game").setExecutor(new GameCommand());
        getCommand("game").setTabCompleter(new GameCommand());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @EventHandler
    public void onCompassRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() != Material.COMPASS) {
            return;
        }

        ItemMeta meta = itemInHand.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String displayName = meta.getDisplayName();
        String expectedName = ColorUtil.get("&f >>&e&l Вернуться в лобби&f <<");

        if (!displayName.equals(expectedName)) {
            return;
        }

        teleportToLobby(player);
    }

    public static void teleportToLobby(Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("lobby");
        p.sendPluginMessage(TheBridgeLCP.getInstance(), "BungeeCord", out.toByteArray());
        p.sendMessage(ColorUtil.get("&aВы были перемещены в лобби"));
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    }
}

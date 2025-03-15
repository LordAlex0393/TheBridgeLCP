package org.lordalex.thebridgelcp.Utils;
import org.bukkit.entity.Player;
import org.lordalex.thebridgelcp.PlayerInfo;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static final Map<Player, PlayerInfo> playersMap = new HashMap<>();

    // Добавление игрока
    public static void addPlayerInfo(Player p, PlayerInfo pi) {
        playersMap.put(p, pi);
    }

    // Удаление игрока
    public static void removePlayerInfo(Player p) {
        playersMap.remove(p);
    }

    // Получение PlayerInfo по игроку
    public static PlayerInfo getPlayerInfo(Player p) {
        return playersMap.get(p);
    }
}

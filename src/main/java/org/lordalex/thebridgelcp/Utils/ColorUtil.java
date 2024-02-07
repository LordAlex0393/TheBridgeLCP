package org.lordalex.thebridgelcp.Utils;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
    public static String getMessage(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}

package me.screescree.SuperiorSteed;

import org.bukkit.ChatColor;

public class Utils {
    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

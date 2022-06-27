package me.screescree.SuperiorSteed.utils;

import org.bukkit.ChatColor;

public class Format {
    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

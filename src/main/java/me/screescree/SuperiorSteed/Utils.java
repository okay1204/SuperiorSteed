package me.screescree.SuperiorSteed;

import org.bukkit.craftbukkit.v1_18_R1.entity.CraftHorse;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class Utils {
    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static Horse getRiddenOrLookedAtHorse(Player player) {

        Entity vehicle = player.getVehicle();
        if (vehicle != null && vehicle instanceof Horse) {
            return (Horse) vehicle;
        }

        RayTraceResult raytraceResult = player.getWorld().rayTraceEntities(
            player.getLocation().add(0, 1, 0),
            player.getLocation().getDirection(),
            10,
            (Entity e) -> e instanceof CraftHorse
        );

        if (raytraceResult == null) {
            return null;
        }

        return (Horse) raytraceResult.getHitEntity();
    }
}

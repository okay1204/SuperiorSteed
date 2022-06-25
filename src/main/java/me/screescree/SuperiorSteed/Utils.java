package me.screescree.SuperiorSteed;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
            entity -> entity.getType() == EntityType.HORSE
        );

        if (raytraceResult == null) {
            return null;
        }

        return (Horse) raytraceResult.getHitEntity();
    }
}

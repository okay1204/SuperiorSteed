package me.screescree.SuperiorSteed;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Utils {
    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void spawnEntity(Entity entity, Location spawnLocation) {
        ServerLevel serverLevel = ((CraftWorld) spawnLocation.getWorld()).getHandle();
        serverLevel.addFreshEntity(entity);
        entity.setPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
    } 
}

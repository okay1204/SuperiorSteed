package me.screescree.SuperiorSteed.SuperiorHorse;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.screescree.SuperiorSteed.SuperiorSteed;
import net.minecraft.server.level.ServerLevel;

public class SuperiorHorse {
    private static SuperiorSteed plugin;

    private SuperiorHorseEntity entity;
    
    // default values
    private double hunger = 1.0;
    private double trust = 0.5;
    private double friendliness = 0.3;
    private double comfortability = 0.2;
    private double waterBravery = 0.1;
    
    public static void setPlugin(SuperiorSteed javaPlugin) {
        plugin = javaPlugin;
    }

    public SuperiorHorse(Location spawnLocation) {
        entity = new SuperiorHorseEntity(spawnLocation.getWorld());

        ServerLevel serverLevel = ((CraftWorld) spawnLocation.getWorld()).getHandle();
        serverLevel.addFreshEntity(entity);
        entity.setPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());

        setHunger(hunger);
        setTrust(trust);
        setFriendliness(friendliness);
        setComfortability(comfortability);
        setWaterBravery(waterBravery);
    }

    private PersistentDataContainer getPersistentDataContainer() {
        CraftEntity craftEntity = CraftEntity.getEntity((CraftServer) Bukkit.getServer(), entity);
        return craftEntity.getPersistentDataContainer();
    }

    private double getDoublePersistentData(String key) {
        PersistentDataContainer container = getPersistentDataContainer();
        return container.get(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
    }

    private void savePersistentData(String key, double value) {
        PersistentDataContainer container = getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE, value);
    }

    public double getHunger() {
        return getDoublePersistentData("hunger");
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
        savePersistentData("hunger", hunger);
    }

    public double getTrust() {
        return getDoublePersistentData("trust");
    }

    public void setTrust(double trust) {
        this.trust = trust;
        savePersistentData("trust", trust);
    }

    public double getFriendliness() {
        return getDoublePersistentData("friendliness");
    }

    public void setFriendliness(double friendliness) {
        this.friendliness = friendliness;
        savePersistentData("friendliness", friendliness);
    }

    public double getComfortability() {
        return getDoublePersistentData("comfortability");
    }

    public void setComfortability(double comfortability) {
        this.comfortability = comfortability;
        savePersistentData("comfortability", comfortability);
    }

    public double getWaterBravery() {
        return getDoublePersistentData("waterBravery");
    }

    public void setWaterBravery(double waterBravery) {
        this.waterBravery = waterBravery;
        savePersistentData("waterBravery", waterBravery);
    }
}

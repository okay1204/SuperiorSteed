package me.screescree.SuperiorSteed.SuperiorHorse;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;

import me.screescree.SuperiorSteed.SuperiorSteed;

public class SuperiorHorse {
    private static SuperiorSteed plugin;

    private SuperiorHorseEntity entity;
    
    // default values
    private Stat hunger;
    private Stat trust;
    private Stat friendliness;
    private Stat comfortability;
    private Stat waterBravery;
    
    public static void setPlugin(SuperiorSteed javaPlugin) {
        plugin = javaPlugin;
    }

    public SuperiorHorse(Location spawnLocation) {
        entity = new SuperiorHorseEntity(spawnLocation.getWorld());
        entity.moveTo(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
        Horse horse = ((CraftWorld) spawnLocation.getWorld()).addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        PersistentDataContainer container = horse.getPersistentDataContainer();
        hunger = new Stat(1.0, container, new NamespacedKey(plugin, "hunger"));
        trust = new Stat(0.5, container, new NamespacedKey(plugin, "trust"));
        friendliness = new Stat(0.3, container, new NamespacedKey(plugin, "friendliness"));
        comfortability = new Stat(0.2, container, new NamespacedKey(plugin, "comfortability"));
        waterBravery = new Stat(0.1, container, new NamespacedKey(plugin, "waterBravery"));
    }
}


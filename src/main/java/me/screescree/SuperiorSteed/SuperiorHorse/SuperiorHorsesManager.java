package me.screescree.SuperiorSteed.superiorhorse;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;

public class SuperiorHorsesManager implements Listener {
    // cache of all superior horses, to retain NMS horse instances
    private HashSet<SuperiorHorse> superiorHorses = new HashSet<SuperiorHorse>();
    private boolean isSpawningCustomHorse = false;

    public SuperiorHorsesManager() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Horse.class)) {
                superiorHorses.add(new SuperiorHorse((Horse) entity));
            }
        }

        int verifyCacheInterval = SuperiorSteed.getInstance().getConfig().getInt("verifyCacheInterval") * 1200;
        if (verifyCacheInterval > 0) {
            Bukkit.getScheduler().runTaskTimer(SuperiorSteed.getInstance(), this::verifyCache, verifyCacheInterval, verifyCacheInterval);
        }
    }

    public void verifyCache() {
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        plugin.getLogger().info("Verifying SuperiorHorse cache...");

        // adds any unregistered horses to the cache
        int added = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Horse.class)) {
                if (getCachedHorse((Horse) entity) == null) {
                    superiorHorses.add(new SuperiorHorse((Horse) entity));
                    added++;
                }
            }
        }

        plugin.getLogger().info("Cache verified! Added " + added + " new horses to cache.");
    }

    public HashSet<SuperiorHorse> getCache() {
        return superiorHorses;
    }

    public boolean checkValidity(SuperiorHorse superiorHorse) {
        if (!superiorHorse.getBukkitEntity().isValid()) {
            return false;
        }
        else {
            return true;
        }
    }

    public SuperiorHorse getCachedHorse(Horse horse) {
        for (SuperiorHorse superiorHorse : superiorHorses) {
            if (superiorHorse.equals(horse)) {
                return superiorHorse;
            }
        }

        return null;
    }

    public void removeHorse(SuperiorHorse horse) {
        superiorHorses.remove(horse);
    }

    public SuperiorHorse getSuperiorHorse(Horse horse) {
        SuperiorHorse superiorHorse = getCachedHorse(horse);
        if (superiorHorse != null) {
            return superiorHorse;
        }
        
        // if horse is not found, create a new one
        isSpawningCustomHorse = true;
        superiorHorse = new SuperiorHorse(horse);
        isSpawningCustomHorse = false;
        superiorHorses.add(superiorHorse);
        return superiorHorse;
    }

    public SuperiorHorse newSuperiorHorse(Location location, SuperiorHorseInfo horseInfo) {
        isSpawningCustomHorse = true;
        SuperiorHorse superiorHorse = new SuperiorHorse(location, horseInfo);
        isSpawningCustomHorse = false;
        superiorHorses.add(superiorHorse);
        return superiorHorse;
    }

    public SuperiorHorse newSuperiorHorse(Location location) {
        return newSuperiorHorse(location, new SuperiorHorseInfo());
    }

    @EventHandler(ignoreCancelled = true)
    private void onHorseSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.DEFAULT || !(event.getEntityType() == EntityType.HORSE) || isSpawningCustomHorse) {
            return;
        }

        Horse horse = ((Horse) event.getEntity());
        isSpawningCustomHorse = true;
        
        // spawn a new horse
        superiorHorses.add(new SuperiorHorse(horse));

        isSpawningCustomHorse = false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHorseDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.HORSE) {
            superiorHorses.remove(getCachedHorse((Horse) event.getEntity()));
        }
    }
}

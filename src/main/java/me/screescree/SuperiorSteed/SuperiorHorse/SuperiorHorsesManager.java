package me.screescree.SuperiorSteed.superiorhorse;

import java.util.ArrayList;

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
import org.bukkit.scheduler.BukkitRunnable;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;

public class SuperiorHorsesManager implements Listener {
    // cache of all superior horses, to retain NMS horse instances
    private ArrayList<SuperiorHorse> superiorHorses = new ArrayList<SuperiorHorse>();
    private boolean isSpawningCustomHorse = false;

    public SuperiorHorsesManager() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Horse.class)) {
                superiorHorses.add(new SuperiorHorse((Horse) entity));
            }
        }

        int cleanupCacheInterval = SuperiorSteed.getInstance().getConfig().getInt("cleanupCacheInterval");
        if (cleanupCacheInterval > 0) {
            long cleanupCacheIntervalTicks = cleanupCacheInterval * 1200;
            new BukkitRunnable() {
                @Override
                public void run() {
                    SuperiorSteed plugin = SuperiorSteed.getInstance();
                    plugin.getLogger().info("Cleaning up SuperiorHorse cache...");
                    cleanupCache();
                    plugin.getLogger().info("Cache cleaned up!");
                }
            }.runTaskTimer(SuperiorSteed.getInstance(), cleanupCacheIntervalTicks, cleanupCacheIntervalTicks);
        }

    }

    public void cleanupCache() {
        int i = 0;
        while (i < superiorHorses.size()) {
            if (!superiorHorses.get(i).getBukkitEntity().isValid()) {
                superiorHorses.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public ArrayList<SuperiorHorse> getCache() {
        return superiorHorses;
    }

    public SuperiorHorse getCachedHorse(Horse horse) {
        for (SuperiorHorse superiorHorse : superiorHorses) {
            if (superiorHorse.equals(horse)) {
                return superiorHorse;
            }
        }

        return null;
    }

    public SuperiorHorse getSuperiorHorse(Horse horse) {
        for (SuperiorHorse superiorHorse : superiorHorses) {
            if (superiorHorse.equals(horse)) {
                return superiorHorse;
            }
        }
        // if horse is not found, create a new one
        isSpawningCustomHorse = true;
        SuperiorHorse superiorHorse = new SuperiorHorse(horse);
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
            for (int i = 0; i < superiorHorses.size(); i++) {
                if (superiorHorses.get(i).equals(event.getEntity())) {
                    superiorHorses.remove(i);
                    break;
                }
            }
        }
    }
}

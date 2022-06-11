package me.screescree.SuperiorSteed.SuperiorHorse;

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
import me.screescree.SuperiorSteed.SuperiorSteed;

public class SuperiorHorsesManager implements Listener {
    private SuperiorSteed plugin;

    // cache of all superior horses, to retain NMS horse instances
    private ArrayList<SuperiorHorse> superiorHorses = new ArrayList<SuperiorHorse>();
    private boolean isSpawningCustomHorse = false;

    public SuperiorHorsesManager(SuperiorSteed plugin) {
        this.plugin = plugin;
        
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(Horse.class)) {
                superiorHorses.add(new SuperiorHorse((Horse) entity));
            }
        }
    }
    
    // TODO add method to clear any horses that are no longer in the server from the cache
    // and make a scheduled task that runs every certain amount of time, configurable via config.yml

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
        SuperiorHorse superiorHorse = new SuperiorHorse(horse);
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
        return newSuperiorHorse(location, SuperiorHorseInfo.DEFAULT);
    }

    @EventHandler(ignoreCancelled = true)
    private void onHorseSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntityType() == EntityType.HORSE) || isSpawningCustomHorse) {
            return;
        }

        Horse horse = ((Horse) event.getEntity());
        isSpawningCustomHorse = true;
        
        // spawn a new horse
        superiorHorses.add(new SuperiorHorse(horse));

        isSpawningCustomHorse = false;
    }

    @EventHandler
    public void onHorseDeath(EntityDeathEvent event) {
        if (event.getEntity().getType().equals(EntityType.HORSE)) {
            for (int i = 0; i < superiorHorses.size(); i++) {
                if (superiorHorses.get(i).equals(event.getEntity())) {
                    superiorHorses.remove(i);
                    break;
                }
            }
        }
    }
}

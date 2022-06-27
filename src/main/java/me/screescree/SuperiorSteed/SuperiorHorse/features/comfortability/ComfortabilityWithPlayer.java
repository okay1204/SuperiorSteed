package me.screescree.SuperiorSteed.superiorhorse.features.comfortability;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class ComfortabilityWithPlayer implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every minute
        return 1200;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // check if there is a player in a 5 block radius of the horse
        boolean isPlayerNearby = false;
        
        for (Entity entity : superiorHorse.getBukkitEntity().getNearbyEntities(5, 5, 5)) {
            if (entity.getType() == EntityType.PLAYER) {
                isPlayerNearby = true;
                break;
            }
        }

        // it should take 1 minute of standing next to a horse to increase comfortability by .01
        // and 1 hour of not standing next to a horse to decrease comfortability by .01
        if (isPlayerNearby) {
            superiorHorse.comfortabilityStat().add(0.01);
        } else {
            superiorHorse.comfortabilityStat().add(-0.00016666666);
        }
    }
}

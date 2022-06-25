package me.screescree.SuperiorSteed.superiorhorse.features.traits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class LonerHandler implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every minute
        return 1200;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // if horse doesn't have loner trait, return
        if (!superiorHorse.getTraits().contains(Trait.LONER)) {
            return;
        }

        // check if there is a horse within a 10 block radius of the horse
        boolean foundHorse = false;

        for (Entity entity : superiorHorse.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
            if (entity.getType() == EntityType.HORSE) {
                foundHorse = true;
                break;
            }
        }

        // if there is a horse nearby, reduce friendliness by .01
        if (foundHorse) {
            superiorHorse.friendlinessStat().add(-0.01);
        }
    }    
}

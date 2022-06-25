package me.screescree.SuperiorSteed.superiorhorse.features.traits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class ExtrovertHandler implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every 5 minutes
        return 6000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // if horse doesn't have extrovert trait, return
        if (!superiorHorse.getTraits().contains(Trait.EXTROVERT)) {
            return;
        }

        // check if there is a horse or player within a 10 block radius of the horse
        boolean foundHorse = false;

        for (Entity entity : superiorHorse.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
            if (entity.getType() == EntityType.HORSE) {
                foundHorse = true;
                break;
            }
        }

        if (foundHorse) {
            superiorHorse.friendlinessStat().add(0.2);
        }
    }
}

package me.screescree.SuperiorSteed.superiorhorse.features.traits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class FriendlyHandler implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every 5 minutes
        return 6000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // if horse doesn't have friendly trait, return
        if (!superiorHorse.getTraits().contains(Trait.FRIENDLY)) {
            return;
        }

        // check if there is a horse or player within a 10 block radius of the horse
        boolean foundFriend = false;

        for (Entity entity : superiorHorse.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
            if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.PLAYER) {
                foundFriend = true;
                break;
            }
        }

        if (foundFriend) {
            superiorHorse.trustStat().add(0.1);
            superiorHorse.friendlinessStat().add(0.1);
        }
    }    
}
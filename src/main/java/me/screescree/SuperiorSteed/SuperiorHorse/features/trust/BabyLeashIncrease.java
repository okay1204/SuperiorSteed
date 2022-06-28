package me.screescree.SuperiorSteed.superiorhorse.features.trust;

import org.bukkit.entity.EntityType;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;

public class BabyLeashIncrease implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every minute
        return 1200;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.getAge() >= SuperiorHorseInfo.AGE_ADULT) {
            return;
        }

        if (!superiorHorse.getBukkitEntity().isLeashed()) {
            return;
        }

        if (superiorHorse.getBukkitEntity().getLeashHolder().getType() != EntityType.PLAYER) {
            return;
        }

        superiorHorse.trustStat().add(0.1);
    }    
}

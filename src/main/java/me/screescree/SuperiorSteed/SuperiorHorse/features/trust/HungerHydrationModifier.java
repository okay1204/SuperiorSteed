package me.screescree.SuperiorSteed.superiorhorse.features.trust;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class HungerHydrationModifier implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every hour
        return 72000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.getBukkitEntity().isTamed()) {
            if (superiorHorse.hungerStat().get() > 0.9 && superiorHorse.hydrationStat().get() > 0.9) {
                superiorHorse.trustStat().add(0.01);
            }
            else if (superiorHorse.hungerStat().get() < 0.2 && superiorHorse.hydrationStat().get() < 0.2) {
                superiorHorse.trustStat().add(-0.01);
            }
        }
    } 
}

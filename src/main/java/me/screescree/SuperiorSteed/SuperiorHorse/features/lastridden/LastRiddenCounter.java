package me.screescree.SuperiorSteed.superiorhorse.features.lastridden;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class LastRiddenCounter implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.getBukkitEntity().isEmpty()) {
            superiorHorse.incrementLastRidden();
        }
        else {
            if (superiorHorse.getLastRidden() > 0) {
                superiorHorse.setLastRidden(0);
            }
        }
    }
}

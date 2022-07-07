package me.screescree.SuperiorSteed.superiorhorse.features.stomach;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class SlowWhenThirsty implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.hydrationStat().get() < 0.3) {
            superiorHorse.setSlowThirst(true);
        }
        else {
            superiorHorse.setSlowThirst(false);
        }
    }    
}

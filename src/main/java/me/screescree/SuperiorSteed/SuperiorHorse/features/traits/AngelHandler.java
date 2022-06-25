package me.screescree.SuperiorSteed.superiorhorse.features.traits;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class AngelHandler implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every 10 minutes
        return 12000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // if horse doesn't have angel trait, return
        if (!superiorHorse.getTraits().contains(Trait.ANGEL)) {
            return;
        }

        superiorHorse.trustStat().add(0.05);
        superiorHorse.friendlinessStat().add(0.05);
        superiorHorse.comfortabilityStat().add(0.05);
    }    
}

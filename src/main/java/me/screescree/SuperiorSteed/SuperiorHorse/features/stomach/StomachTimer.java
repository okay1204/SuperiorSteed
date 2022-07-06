package me.screescree.SuperiorSteed.superiorhorse.features.stomach;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;

public class StomachTimer implements LoopingTask<SuperiorHorse> {

    @Override
    public int getIntervalTicks() {
        // run every hour
        return 72000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.getOwner() == null) {
            return;
        }

        double decrementAmount = superiorHorse.getTraits().contains(Trait.HARD_KEEPER) ? -0.03 : -0.01;
    
        superiorHorse.hungerStat().add(decrementAmount);
        superiorHorse.hydrationStat().add(decrementAmount);

        if (superiorHorse.isPregnant()) {
            superiorHorse.pregnancyComplicationStat().add(-0.1);
        }
    }
    
}

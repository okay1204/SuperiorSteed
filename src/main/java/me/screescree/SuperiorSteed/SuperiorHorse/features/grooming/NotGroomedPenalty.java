package me.screescree.SuperiorSteed.superiorhorse.features.grooming;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class NotGroomedPenalty implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every hour
        return 72000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (!superiorHorse.isFinishedGrooming()) {
            superiorHorse.comfortabilityStat().add(-0.05);
            superiorHorse.friendlinessStat().add(-0.02);
        }
    }
}

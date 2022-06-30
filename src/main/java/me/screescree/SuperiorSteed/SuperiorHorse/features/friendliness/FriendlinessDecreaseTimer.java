package me.screescree.SuperiorSteed.superiorhorse.features.friendliness;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class FriendlinessDecreaseTimer implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every hour
        return 72000;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        // if the horse has not been ridden for 24 hours and is not pregnant, decrease friendliness by 0.1
        if (superiorHorse.getLastRidden() > 1728000 && !superiorHorse.isPregnant()) {
            superiorHorse.friendlinessStat().add(-0.1);
        }
    }
}

package me.screescree.SuperiorSteed.superiorhorse.features.friendliness;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class FriendlinessIncreaseIfPlayerRiding implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every minute
        return 1200;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (!superiorHorse.getBukkitEntity().isEmpty()) {
            superiorHorse.friendlinessStat().add(0.01);
        }
    }
}

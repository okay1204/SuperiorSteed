package me.screescree.SuperiorSteed.superiorhorse.features.friendliness;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class DecrementAttackByHorseTimer implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        superiorHorse.decrementAttackedByHorseTimer();
    }
}

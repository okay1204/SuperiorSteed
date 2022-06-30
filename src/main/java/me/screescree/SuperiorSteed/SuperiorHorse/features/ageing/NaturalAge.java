package me.screescree.SuperiorSteed.superiorhorse.features.ageing;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;

public class NaturalAge implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }
    
    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        superiorHorse.incrementAge();

        // prevent baby horses from growing up to adult horses
        if (superiorHorse.getAge() < SuperiorHorseInfo.AGE_ADULT) {
            superiorHorse.getBukkitEntity().setAge(-24000);
        }
    }
}

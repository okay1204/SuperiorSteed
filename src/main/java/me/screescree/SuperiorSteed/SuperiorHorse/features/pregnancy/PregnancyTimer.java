package me.screescree.SuperiorSteed.superiorhorse.features.pregnancy;

import org.bukkit.Sound;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.BirthInfo;
import me.screescree.SuperiorSteed.superiorhorse.Births;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class PregnancyTimer implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        return 1;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        if (superiorHorse.isPregnant()) {
            superiorHorse.setPregnancyTimer(superiorHorse.getPregnancyTimer() - 1);

            if (!superiorHorse.isPregnant()) {
                giveBirth(superiorHorse);
            }
        }
    }

    public void giveBirth(SuperiorHorse superiorHorse) {
        double pregnancyComplication = superiorHorse.pregnancyComplicationStat().get();

        if (ThreadLocalRandom.current().nextDouble() < pregnancyComplication) {
            Births.add(new BirthInfo(superiorHorse.getBukkitEntity().getLocation(), superiorHorse.getPregnantWith()));
        }
        else {
            superiorHorse.getBukkitEntity().getWorld().playSound(superiorHorse.getBukkitEntity().getLocation(), Sound.ENTITY_COW_MILK, 1, 1);
        }

        superiorHorse.setPregnantWith(null);
    }
}

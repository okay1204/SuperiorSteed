package me.screescree.SuperiorSteed.superiorhorse.features.stomach;

import org.bukkit.Bukkit;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class StomachHurt implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // run every 3 seconds
        return 60;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        double determiningHurtAmount = 0;

        if (isZero(superiorHorse.hungerStat().get())) {
            determiningHurtAmount += 1;
        }
        if (isZero(superiorHorse.hydrationStat().get())) {
            determiningHurtAmount += 1;
        }

        if (determiningHurtAmount > 0) {
            final double hurtAmount = determiningHurtAmount;
            Bukkit.getScheduler().runTaskLater(SuperiorSteed.getInstance(), () -> {;
                if (superiorHorse.getBukkitEntity().isValid()) {
                    superiorHorse.getBukkitEntity().damage(hurtAmount, null);

                    // stop the horse from pausing to stand up if it's hurt because of starvation or dehydration
                    Bukkit.getScheduler().runTaskLater(SuperiorSteed.getInstance(), () -> {
                        superiorHorse.getNMSEntity().setStanding(false);
                    }, 1);
                }
            }, superiorHorse.getStomachHurtDelay());
            // we use a random delay so that all horses dont get damaged at the same time, that'll look weird
        }
    }

    private boolean isZero(double value) {
        return Math.abs(value) < 0.0001;
    }
}

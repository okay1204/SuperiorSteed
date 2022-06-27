package me.screescree.SuperiorSteed.superiorhorse.features.waterbravery;

import org.bukkit.Material;
import org.bukkit.entity.Horse;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class WaterCheck implements LoopingTask<SuperiorHorse> {
    
    @Override
    public int getIntervalTicks() {
        // check every 10 seconds
        return 200;
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        Horse horse = superiorHorse.getBukkitEntity();
        if (horse.isInWater()) {
            if (superiorHorse.waterBraveryStat().get() < 0.5) {
                superiorHorse.trustStat().add(-0.01);
                superiorHorse.comfortabilityStat().add(-0.01);
            }
        }
        else {
            // check if water is nearby
            for (int xOffset = -2; xOffset <= 2; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    for (int zOffset = -2; zOffset <= 2; zOffset++) {
                        if (horse.getWorld().getBlockAt(horse.getLocation().add(xOffset, yOffset, zOffset)).getType() == Material.WATER) {
                            superiorHorse.waterBraveryStat().add(0.00166666667);
                            return;
                        }
                    }
                }
            }
        }

    }
}

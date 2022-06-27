package me.screescree.SuperiorSteed.superiorhorse.features.waterbravery;

import org.bukkit.Material;
import org.bukkit.entity.Horse;

import me.screescree.SuperiorSteed.LoopingTask;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class WaterScare implements LoopingTask<SuperiorHorse> {
    @Override
    public int getIntervalTicks() {
        // check every tick
        return 1;
    }

    private double waterBraverySpeed(double waterBravery) {
        return Math.max(0, (waterBravery * waterBravery) - 0.04);
    }

    @Override
    public void runLoopingTask(SuperiorHorse superiorHorse) {
        Horse horse = superiorHorse.getBukkitEntity();

        // if the horse is not being ridden, reset water bravery
        if (horse.isEmpty()) {
            superiorHorse.setWaterBraveryFactor(1);
            return;
        }

        // check if water is nearby
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -2; zOffset <= 2; zOffset++) {
                    if (horse.getWorld().getBlockAt(horse.getLocation().add(xOffset, yOffset, zOffset)).getType() == Material.WATER) {
                        // if there is, decrease movement speed
                        superiorHorse.setWaterBraveryFactor(waterBraverySpeed(superiorHorse.waterBraveryStat().get()));
                        return;
                    }
                }
            }
        }

        // if there isn't, reset movement speed
        superiorHorse.setWaterBraveryFactor(1);
    }
}

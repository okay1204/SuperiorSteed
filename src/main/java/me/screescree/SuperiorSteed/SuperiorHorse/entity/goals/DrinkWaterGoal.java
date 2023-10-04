package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class DrinkWaterGoal extends ConsumeGoal {
    private double startConsumingMin;

    public DrinkWaterGoal(SuperiorHorseEntity superiorHorse, double speedModifier) {
        super(superiorHorse, speedModifier);
        randomizeStartConsumingMin();
    }

    @Override
    protected boolean isDesiredBlock(Block block) {
        // Check if block is air
        if (!block.getType().isAir()) {
            return false;
        }

        // If the horse is an adult, it's hitbox is bigger so we can check if the air block directly above the water
        Block blockBelow = block.getRelative(BlockFace.DOWN);
        if (mob.getWrapper().getAge() >= SuperiorHorseInfo.AGE_ADULT) {
            if (isDrinkable(blockBelow)) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        if (blockBelow.getRelative(xOffset, 0, zOffset).getType().isSolid()) {
                            return true;
                        }
                    }
                }
            }
        }
        // if it's a baby, we must check the side blocks instead
        else {
            if (blockBelow.getType().isSolid()) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        if (isDrinkable(blockBelow.getRelative(xOffset, 0, zOffset))) {
                            return true;
                        }
                    }
                }
            }
        }

        // Check if drinkable source is directly next to the block
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int zOffset = -1; zOffset <= 1; zOffset++) {
                if (isDrinkable(block.getRelative(xOffset, 0, zOffset))) {
                    return true;
                }
            }
        }

        return false;
    }

    // checks if nearby blocks are water, and returns the water block if so. Returns null if not.
    @Override
    protected BlockPos getConsumableSourcePos() {

        BlockPos mobBlockPos = mob.blockPosition();
        Vec3 mobPos = mob.position();

        BlockPos closestWaterPos = null;
        Vec3 closestVec = null;

        // Checks for a drinkable source within a radius of 2 blocks underneath or around the horse
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 0; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos waterPos = mobBlockPos.offset(xOffset, yOffset, zOffset);
                    
                    Block block = ((World) mob.level().getWorld()).getBlockAt(waterPos.getX(), waterPos.getY(), waterPos.getZ());
                    if (isDesiredBlock(block)) {
                        // keep track of water pos that is closest to the horse
                        if (
                            closestWaterPos == null ||
                            mobPos.distanceTo(new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ())) < mobPos.distanceTo(closestVec)
                        ) {
                            closestWaterPos = waterPos;
                            closestVec = new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ());
                        }
                    }
                }
            }
        }

        return closestWaterPos;
    }

    @Override
    public boolean canStart() {
        SuperiorHorse wrapper = mob.getWrapper();
        if (wrapper.hydrationStat() == null || wrapper.hydrationStat().get() > startConsumingMin) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canContinue() {
        SuperiorHorse wrapper = mob.getWrapper();
        if (Math.abs(wrapper.hydrationStat().get() - 1.0) < 0.001) {
            return false;
        }

        return true;
    }

    private void randomizeStartConsumingMin() {
        startConsumingMin = mob.randomNextDouble(0.9, 0.98);
    }

    @Override
    public void stop() {
        super.stop();
        randomizeStartConsumingMin();
    }

    protected void increaseStat(BlockPos sourcePos) {
        mob.getWrapper().hydrationStat().add(0.01);
    }

    private boolean isDrinkable(Block block) {
        if (block.getType() == Material.WATER) {
            return true;
        }

        if (block.getType() == Material.WATER_CAULDRON) {
            return true;
        }

        if (block.getBlockData() instanceof Waterlogged && ((Waterlogged) block.getBlockData()).isWaterlogged()) {
            return true;
        }

        return false;
    }
}
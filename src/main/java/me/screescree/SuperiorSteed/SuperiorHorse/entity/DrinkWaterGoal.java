package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.material.Material;

public class DrinkWaterGoal extends Goal {
    private final int MAX_DISTANCE = 20;

    protected final SuperiorHorseEntity mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected BlockPos targetPos;
    private double hydrationLimit;

    public DrinkWaterGoal(SuperiorHorseEntity superiorHorse, double d0) {
        mob = superiorHorse;
        speedModifier = d0;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        randomizeHydrationLimit();
     }

    @Override
    public boolean canUse() {
        if (mob.getWrapper().hydrationStat().get() > hydrationLimit) {
            return false;
        }

        BlockPos blockposition = lookForCoastalBlock();
        if (blockposition != null) {
           posX = blockposition.getX();
           posY = blockposition.getY();
           posZ = blockposition.getZ();
           return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        BlockPos blockposition = lookForCoastalBlock();
        if (blockposition == null) {
            return false;
        }
        else {
            posX = blockposition.getX();
            posY = blockposition.getY();
            posZ = blockposition.getZ();
        }

        return mob.getWrapper().hydrationStat().get() < 1.0;
    }

    public void tick() {
        BlockPos waterPos = getDrinkableBlock(mob.blockPosition(), 2);
        if (waterPos == null) {
            mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(false);
        }
        else if (mob.getNavigation().isDone()) {
            mob.getLookControl().setLookAt(waterPos.getX(), waterPos.getY() - 5, waterPos.getZ());
            mob.getWrapper().hydrationStat().add(0.01);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(true);
        }
    }

    public void start() {
        mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
    }

    public void stop() {
        randomizeHydrationLimit();
    }

    private void randomizeHydrationLimit() {
        hydrationLimit = mob.getRandom().nextDouble(0.9, 0.98);
    }

    protected BlockPos lookForCoastalBlock() {
        BlockPos blockposition = mob.blockPosition();
        return !mob.level.getBlockState(blockposition).getCollisionShape(mob.level, blockposition).isEmpty() ? null : BlockPos.findClosestMatch(blockposition, MAX_DISTANCE, 1, blockPos -> {
            // Check if block is air
            if (mob.level.getBlockState(blockPos).getMaterial() != Material.AIR) {
                return false;
            }
            
            // Check if block underneath is water
            if (mob.level.getBlockState(blockPos.offset(0, -1, 0)).getMaterial() != Material.WATER) {
                return false;
            }

            // Check if at least one block around the water is solid
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (mob.level.getBlockState(blockPos.offset(xOffset, -1, zOffset)).getMaterial().isSolid()) {
                        return true;
                    }
                }
            }
            return false;
        }).orElse(null);
    }

    // checks if nearby blocks are water, and returns the water block if so. Returns null if not.
    protected BlockPos getDrinkableBlock(BlockPos blockPos, int radius) {
        for (double xOffset = -radius; xOffset <= radius; xOffset++) {
            for (double zOffset = -radius; zOffset <= radius; zOffset++) {
                BlockPos waterPos = blockPos.offset(xOffset, -1, zOffset);
                if (mob.level.getBlockState(waterPos).getMaterial() == Material.WATER) {
                    return waterPos;
                }
            }
        }

        return null;
    }
}
package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import java.util.ArrayList;

import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

public class DrinkWaterGoal extends ConsumeGoal {
    private final int MAX_DISTANCE = 20;

    public DrinkWaterGoal(SuperiorHorseEntity superiorHorse, double d0) {
        super(superiorHorse, () -> superiorHorse.getWrapper().hydrationStat(), d0);
    }

    @Override
    protected BlockPos lookForSuitablePos() {
        BlockPos blockposition = mob.blockPosition();
        return !mob.level.getBlockState(blockposition).getCollisionShape(mob.level, blockposition).isEmpty() ? null : BlockPos.findClosestMatch(blockposition, MAX_DISTANCE, 1, blockPos -> {
            // Check if block is air
            if (mob.level.getBlockState(blockPos).getMaterial() != Material.AIR) {
                return false;
            }

            // Check if block underneath is drinkable and one block around it is solid
            BlockState blockStatBelow = mob.level.getBlockState(blockPos.below());
            if (isDrinkable(blockStatBelow)) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        if (mob.level.getBlockState(blockPos.offset(xOffset, -1, zOffset)).getMaterial().isSolid()) {
                            return true;
                        }
                    }
                }
            }

            // Check if drinkable source is directly next to the block
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockState blockState = mob.level.getBlockState(blockPos.offset(xOffset, 0, zOffset));
                    // check if the block is water, waterlogged, or a water cauldron
                    if (isDrinkable(blockState)) {
                        return true;
                    }
                }
            }

            return false;
        }).orElse(null);
    }

    // checks if nearby blocks are water, and returns the water block if so. Returns null if not.
    @Override
    protected BlockPos getConsumableSourcePos() {
        BlockPos mobBlockPos = mob.blockPosition();

        ArrayList<BlockPos> waterPosList = new ArrayList<BlockPos>();

        // Checks for a drinkable source within a radius of 2 blocks underneath or around the horse
        for (int xOffset = -3; xOffset <= 3; xOffset++) {
            for (int yOffset = -1; yOffset <= 0; yOffset++) {
                for (int zOffset = -3; zOffset <= 3; zOffset++) {
                    BlockPos waterPos = mobBlockPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = mob.level.getBlockState(waterPos);
                    if (isDrinkable(blockState)) {
                        waterPosList.add(waterPos);
                    }
                }
            }
        }

        if (waterPosList.isEmpty()) {
            return null;
        }
        else {
            // return water pos that is closest to the horse
            BlockPos closestWaterPos = waterPosList.get(0);
            Vec3 closestVec = new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ());
            Vec3 mobPos = mob.position();
            // iterate through water pos list except for the first one, and find the closest one to the horse
            for (int i = 1; i < waterPosList.size(); i++) {
                BlockPos waterPos = waterPosList.get(i);
                if (mobPos.distanceTo(new Vec3(waterPos.getX(), waterPos.getY(), waterPos.getZ())) < mobPos.distanceTo(closestVec)) {
                    closestWaterPos = waterPos;
                    closestVec = new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ());
                }
            }

            return closestWaterPos;
        }
    }

    protected double randomizeStartConsumingLimit() {
        return mob.getRandom().nextDouble(0.9, 0.98);
    }

    protected void increaseStat(Stat stat, BlockPos sourcePos) {
        stat.add(0.01);
    }

    private boolean isDrinkable(BlockState blockState) {
        return blockState.getMaterial() == Material.WATER || blockState.getFluidState().is(FluidTags.WATER) || blockState.is(Blocks.WATER_CAULDRON);
    }
}
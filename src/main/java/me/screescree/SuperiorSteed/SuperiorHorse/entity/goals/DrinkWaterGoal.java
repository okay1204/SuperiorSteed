package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import java.util.ArrayList;

import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.core.BlockPos;
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
    @Override
    protected BlockPos getConsumableSourcePos(BlockPos blockPos) {
        ArrayList<BlockPos> waterPosList = new ArrayList<BlockPos>();
        for (double xOffset = -2; xOffset <= 2; xOffset++) {
            for (double zOffset = -2; zOffset <= 2; zOffset++) {
                BlockPos waterPos = blockPos.offset(xOffset, -1, zOffset);
                if (mob.level.getBlockState(waterPos).getMaterial() == Material.WATER) {
                    waterPosList.add(waterPos);
                }
            }
        }

        if (waterPosList.isEmpty()) {
            return null;
        }
        else {
            // return water pos that is closest to the horse
            BlockPos closestWaterPos = waterPosList.get(0);
            Vec3 closestWarVec = new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ());
            Vec3 mobPos = mob.position();
            // iterate through water pos list except for the first one, and find the closest one to the horse
            for (int i = 1; i < waterPosList.size(); i++) {
                BlockPos waterPos = waterPosList.get(i);
                if (mobPos.distanceTo(new Vec3(waterPos.getX(), waterPos.getY(), waterPos.getZ())) < mobPos.distanceTo(closestWarVec)) {
                    closestWaterPos = waterPos;
                    closestWarVec = new Vec3(closestWaterPos.getX(), closestWaterPos.getY(), closestWaterPos.getZ());
                }
            }

            return closestWaterPos;
        }
    }
}
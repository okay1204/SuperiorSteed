package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import java.util.ArrayList;
import java.util.Set;

import me.screescree.SuperiorSteed.listeners.BrewingSeeds;
import me.screescree.SuperiorSteed.superiorhorse.Stat;
import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.phys.Vec3;

public class EatSeedsGoal extends ConsumeGoal {
    // if updating this, be sure to update BrewingSeeds.java as well
    public final Set<String> SEEDS = Set.of("wheat_seeds", "pumpkin_seeds", "melon_seeds", "beetroot_seeds");

    private final int MAX_DISTANCE = 20;

    public EatSeedsGoal(SuperiorHorseEntity superiorHorse, double d0) {
        super(superiorHorse, () -> superiorHorse.getWrapper().hungerStat(), d0);
    }

    @Override
    protected BlockPos lookForSuitablePos() {
        BlockPos blockposition = mob.blockPosition();
        return !mob.level.getBlockState(blockposition).getCollisionShape(mob.level, blockposition).isEmpty() ? null : BlockPos.findClosestMatch(blockposition, MAX_DISTANCE, 1, blockPos -> {
            // check if the block is a brewing stand
            if (mob.level.getBlockState(blockPos).is(Blocks.BREWING_STAND)) {
                // check if the brewing stand has seeds in it
                BrewingStandBlockEntity brewingStandEntity = (BrewingStandBlockEntity) mob.level.getBlockEntity(blockPos);
                for (int i = 0; i <= 2; i++) {
                    if (SEEDS.contains(brewingStandEntity.getItem(i).getItem().toString())) {
                        return true;
                    }
                }
            }

            return false;
        }).orElse(null);
    }

    // checks if nearby blocks are brewing stands, and returns the brewing stand block if so. Returns null if not.
    @Override
    protected BlockPos getConsumableSourcePos() {
        BlockPos mobBlockPos = mob.blockPosition();

        ArrayList<BlockPos> brewingStandPosList = new ArrayList<BlockPos>();
        for (double xOffset = -2; xOffset <= 2; xOffset++) {
            for (double zOffset = -2; zOffset <= 2; zOffset++) {
                BlockPos brewingPos = mobBlockPos.offset(xOffset, 0, zOffset);
                if (mob.level.getBlockState(brewingPos).is(Blocks.BREWING_STAND)) {
                    brewingStandPosList.add(brewingPos);
                }
            }
        }

        if (brewingStandPosList.isEmpty()) {
            return null;
        }
        else {
            // return brewing stand pos that is closest to the horse
            BlockPos closestBrewingPos = brewingStandPosList.get(0);
            Vec3 closestVec = new Vec3(closestBrewingPos.getX() + 0.5, closestBrewingPos.getY() + 0.5, closestBrewingPos.getZ() + 0.5);
            Vec3 mobPos = mob.position();
            // iterate through water pos list except for the first one, and find the closest one to the horse
            for (int i = 1; i < brewingStandPosList.size(); i++) {
                BlockPos brewingPos = brewingStandPosList.get(i);
                if (mobPos.distanceTo(new Vec3(brewingPos.getX() + 0.5, brewingPos.getY() + 0.5, brewingPos.getZ() + 0.5)) < mobPos.distanceTo(closestVec)) {
                    closestBrewingPos = brewingPos;
                    closestVec = new Vec3(closestBrewingPos.getX() + 0.5, closestBrewingPos.getY() + 0.5, closestBrewingPos.getZ() + 0.5);
                }
            }

            return closestBrewingPos;
        }
    }

    @Override
    public void tick() {
        // if the horse is standing on a brewing stand, teleport the horse 1 block down
        Vec3 mobPos = mob.position();

        // if the mob's Y level ends with about .875, then it is possible that it is standing on a brewing stand
        if (Math.abs((mobPos.y() % 1.0) - 0.875) < 0.001) {
            BlockPos mobBlockPos = new BlockPos(mobPos);
            // check for a brewing stand in a 3x3 radius around the horse
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos brewingPos = mobBlockPos.offset(xOffset, 0, zOffset);

                    // if a brewing stand is found, teleport the horse .75 blocks down
                    if (mob.level.getBlockState(brewingPos).is(Blocks.BREWING_STAND)) {
                        mob.getWrapper().getBukkitEntity().teleport(mob.getWrapper().getBukkitEntity().getLocation().add(0, -0.75, 0));
                        break;
                    }
                }
            }
            
        }
        
        super.tick();
    }

    protected double randomizeStartConsumingLimit() {
        return mob.getRandom().nextDouble(0.85, 0.9);
    }

    protected void increaseStat(Stat stat, BlockPos sourcePos) {
        stat.add(0.1);

        org.bukkit.block.Block block = ((org.bukkit.World) mob.level.getWorld()).getBlockAt(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ());
        org.bukkit.inventory.BrewerInventory inventory = ((org.bukkit.block.BrewingStand) block.getState()).getInventory();
        for (int i = 0; i <= 2; i++) {
            org.bukkit.inventory.ItemStack item = inventory.getItem(i);
            if (item != null) {
                if (BrewingSeeds.SEEDS.contains(inventory.getItem(i).getType())) {
                    inventory.setItem(i, null);
                    break;
                }
            }
        }
    }
}
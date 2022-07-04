package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.Seed;
import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EatSeedsGoal extends ConsumeGoal {
    private Set<Material> edibleSeeds;

    public EatSeedsGoal(SuperiorHorseEntity superiorHorse, double d0) {
        super(superiorHorse, () -> superiorHorse.getWrapper().hungerStat(), d0);
        setDesiredBlockPredicate(this::isDesiredBlock);
    }

    private boolean isDesiredBlock(Block block) {
        if (block.getType() != Material.BREWING_STAND) {
            return false;
        }

        updateEdibleSeeds();
        
        return hasEdibleSeed((BrewingStand) block.getState());
    }

    private void updateEdibleSeeds() {
        SuperiorHorse wrapper = mob.getWrapper();

        edibleSeeds = new HashSet<>();
        if (!wrapper.getTraits().contains(Trait.PICKY_EATER)) {
            edibleSeeds = new HashSet<>(Seed.MATERIALS);
        }
        else {
            edibleSeeds.add(Seed.ALWAYS_LIKED.getMaterial());
            edibleSeeds.add(wrapper.getFavoriteSeed().getMaterial());
        }
    }

    public boolean hasEdibleSeed(BrewingStand brewingStand) {
        BrewerInventory inventory = brewingStand.getInventory();

        for (int i = 0; i <= 2; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                if (edibleSeeds.contains(inventory.getItem(i).getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    // checks if nearby blocks are brewing stands, and returns the brewing stand block if so. Returns null if not.
    @Override
    protected BlockPos getConsumableSourcePos() {
        BlockPos mobBlockPos = mob.blockPosition();
        Vec3 mobPos = mob.position();

        BlockPos closestBrewingPos = null;
        Vec3 closestVec = null;

        updateEdibleSeeds();

        for (double xOffset = -2; xOffset <= 2; xOffset++) {
            for (double zOffset = -2; zOffset <= 2; zOffset++) {

                BlockPos brewingPos = mobBlockPos.offset(xOffset, 0, zOffset);
                if (mob.level.getBlockState(brewingPos).is(Blocks.BREWING_STAND)) {

                    // check if the brewing stand contains edible seeds
                    Block block = ((World) mob.level.getWorld()).getBlockAt(brewingPos.getX(), brewingPos.getY(), brewingPos.getZ());
                    if (hasEdibleSeed((BrewingStand) block.getState())) {
                        // keep track of brewing stand pos that is closest to the horse
                        if (
                            closestBrewingPos == null ||
                            mobPos.distanceTo(new Vec3(brewingPos.getX(), brewingPos.getY(), brewingPos.getZ())) < mobPos.distanceTo(closestVec)
                        ) {
                            closestBrewingPos = brewingPos;
                            closestVec = new Vec3(closestBrewingPos.getX(), closestBrewingPos.getY(), closestBrewingPos.getZ());
                        }
                    }
                }
            }
        }

        return closestBrewingPos;
    }

    @Override
    public void tick() {
        // if the horse is standing on a brewing stand, teleport the horse down
        Vec3 mobPos = mob.position();

        // if the mob's Y level ends with about .875, then it is possible that it is standing on a brewing stand
        if (Math.abs((mobPos.y() % 1.0) - 0.875) < 0.001) {
            BlockPos mobBlockPos = new BlockPos(mobPos);
            // check for a brewing stand in a 3x3 radius around the horse
            boolean brewingStandFound = false;

            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos brewingPos = mobBlockPos.offset(xOffset, 0, zOffset);

                    // if a brewing stand is found, teleport the horse .75 blocks down
                    if (mob.level.getBlockState(brewingPos).is(Blocks.BREWING_STAND)) {
                        Horse horse = mob.getWrapper().getBukkitEntity();
                        horse.teleport(horse.getLocation().add(0, -0.75, 0));
                        brewingStandFound = true;
                        break;
                    }
                }

                if (brewingStandFound) {
                    break;
                }
            }
            
        }
        
        super.tick();
    }

    protected double randomizeStartConsumingLimit() {
        return mob.getRandom().nextDouble(0.9, 0.95);
    }

    protected void increaseStat(Stat stat, BlockPos sourcePos) {
        stat.add(0.05);

        Block block = ((World) mob.level.getWorld()).getBlockAt(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ());
        BrewerInventory inventory = ((BrewingStand) block.getState()).getInventory();

        for (int i = 0; i <= 2; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                if (edibleSeeds.contains(inventory.getItem(i).getType())) {
                    inventory.setItem(i, null);
                    break;
                }
            }
        }
    }
}
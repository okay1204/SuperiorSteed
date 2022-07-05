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
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EatSeedsGoal extends ConsumeGoal {
    private Set<Material> edibleSeeds;
    private double startConsumingMin;

    public EatSeedsGoal(SuperiorHorseEntity superiorHorse, double speedModifier) {
        super(superiorHorse, speedModifier);
        randomizeStartConsumingMin();
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
                if (!hungerIsFull() && edibleSeeds.contains(inventory.getItem(i).getType())) {
                    return true;
                }
                else if (!pregnancyComplicationIsFull() && inventory.getItem(i).getType() == Material.BEETROOT_SEEDS) {
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

    @Override
    public boolean canUse() {
        SuperiorHorse wrapper = mob.getWrapper();
        if (wrapper.hungerStat() == null) {
            return false;
        }

        if (
            wrapper.hungerStat().get() > startConsumingMin &&
            (!wrapper.isPregnant() || wrapper.pregnancyComplicationStat().get() > startConsumingMin)
        ) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (hungerIsFull() && pregnancyComplicationIsFull()) {
            return false;
        }

        return super.canContinueToUse();
    }

    private void randomizeStartConsumingMin() {
        startConsumingMin = mob.getRandom().nextDouble(0.9, 0.95);
    }

    @Override
    public void stop() {
        super.stop();
        randomizeStartConsumingMin();
    }

    private boolean hungerIsFull() {
        return Math.abs(mob.getWrapper().hungerStat().get() - 1.0) < 0.001;
    }

    private boolean pregnancyComplicationIsFull() {
        return !mob.getWrapper().isPregnant() || Math.abs(mob.getWrapper().pregnancyComplicationStat().get() - 1.0) < 0.001;
    }

    protected void increaseStat(BlockPos sourcePos) {
        SuperiorHorse wrapper = mob.getWrapper();

        Block block = ((World) mob.level.getWorld()).getBlockAt(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ());
        BrewerInventory inventory = ((BrewingStand) block.getState()).getInventory();

        if (!hungerIsFull()) {
            for (int i = 0; i <= 2; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    if (edibleSeeds.contains(inventory.getItem(i).getType())) {
                        wrapper.hungerStat().add(0.05);
                        if (wrapper.isPregnant() && inventory.getItem(i).getType() == Material.BEETROOT_SEEDS) {
                            wrapper.pregnancyComplicationStat().add(0.05);
                        }
                        
                        inventory.setItem(i, null);
                        break;
                    }
                }
            }
        }
        else if (!pregnancyComplicationIsFull()) {
            for (int i = 0; i <= 2; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    if (inventory.getItem(i).getType() == Material.BEETROOT_SEEDS) {
                        wrapper.pregnancyComplicationStat().add(0.05);

                        inventory.setItem(i, null);
                        break;
                    }
                }
            }
        }
    }
}
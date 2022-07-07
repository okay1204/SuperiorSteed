package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.ConsumeGoal;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.core.BlockPos;

public class EatGrassGoal extends ConsumeGoal {
    private double startConsumingMin;

    public EatGrassGoal(SuperiorHorseEntity superiorHorse, double speedModifier) {
        super(superiorHorse, speedModifier);
        randomizeStartConsumingMin();
    }

    @Override
    protected boolean isDesiredBlock(Block block) {
        return !block.getType().isSolid() && block.getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK;
    }

    // checks if nearby blocks are brewing stands, and returns the brewing stand block if so. Returns null if not.
    @Override
    protected BlockPos getConsumableSourcePos() {
        BlockPos belowPos = mob.blockPosition().below();

        return ((World) mob.level.getWorld()).getBlockAt(belowPos.getX(), belowPos.getY(), belowPos.getZ()).getType() == Material.GRASS_BLOCK ? belowPos : null;
    }

    @Override
    public boolean canStart() {
        SuperiorHorse wrapper = mob.getWrapper();
        if (wrapper.hungerStat() == null) {
            return false;
        }

        if (wrapper.hungerStat().get() > startConsumingMin) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canContinue() {
        if (hungerIsFull()) {
            return false;
        }

        return true;
    }

    private void randomizeStartConsumingMin() {
        startConsumingMin = mob.getRandom().nextDouble(0.85, 0.9);
    }

    @Override
    public void stop() {
        super.stop();
        randomizeStartConsumingMin();
    }

    private boolean hungerIsFull() {
        return Math.abs(mob.getWrapper().hungerStat().get() - 1.0) < 0.001;
    }

    protected void increaseStat(BlockPos sourcePos) {
        mob.getWrapper().hungerStat().add(0.01);
    }
}

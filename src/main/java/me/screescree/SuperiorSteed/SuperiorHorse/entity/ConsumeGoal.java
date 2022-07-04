package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;
import java.util.function.Predicate;

import org.bukkit.World;
import org.bukkit.block.Block;

import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public abstract class ConsumeGoal extends Goal {
    private final static int MAX_DISTANCE = 20;

    protected final SuperiorHorseEntity mob;
    private final double speedModifier;
    private final GetStatFunction getStatFunction;
    private Predicate<Block> desiredBlockPredicate;
    private Block targetBlock;
    private double startConsumingLimit;
    private Vec3 lastTickPos;
    private int ticksSinceLastMove = 0;

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, GetStatFunction statFunction, double d0, Predicate<Block> desiredBlockPredicate) {
        mob = superiorHorse;
        speedModifier = d0;
        this.getStatFunction = statFunction;
        startConsumingLimit = randomizeStartConsumingLimit();
        this.desiredBlockPredicate = desiredBlockPredicate;
        lastTickPos = mob.position();
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, GetStatFunction statFunction, double d0) {
        this(superiorHorse, statFunction, d0, null);
    }

    protected void setDesiredBlockPredicate(Predicate<Block> desiredBlock) {
        this.desiredBlockPredicate = desiredBlock;
    }

    @Override
    public boolean canUse() {
        if (desiredBlockPredicate == null) {
            return false;
        }

        if (getStatFunction.getStat() == null) {
            return false;
        }
        
        if (getStatFunction.getStat().get() > startConsumingLimit) {
            return false;
        }

        BlockPos foundBlockPos = findTargetBlock();
        if (foundBlockPos == null) {
            return false;
        }

        targetBlock = ((World) mob.level.getWorld()).getBlockAt(foundBlockPos.getX(), foundBlockPos.getY(), foundBlockPos.getZ());
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (Math.abs(getStatFunction.getStat().get() - 1.0) < 0.001) {
            return false;
        }

        
        BlockPos foundBlockPos = findTargetBlock();
        if (foundBlockPos == null) {
            if (desiredBlockPredicate.test(targetBlock)) {
                Path path = mob.getNavigation().getPath();
                if (path != null && !path.isDone()) {
                    return true;
                }
            }

            return false;
        }

        targetBlock = ((World) mob.level.getWorld()).getBlockAt(foundBlockPos.getX(), foundBlockPos.getY(), foundBlockPos.getZ());
        return true;
    }

    public BlockPos findTargetBlock() {
        BlockPos foundBlockPos = BlockPos.findClosestMatch(mob.blockPosition(), MAX_DISTANCE, 5, blockPos -> {
            boolean isDesiredBlock = desiredBlockPredicate.test(((World) mob.level.getWorld()).getBlockAt(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            if (isDesiredBlock) {
                Path path = mob.getNavigation().createPath(blockPos, 0);
                if (path == null || path.canReach()) {
                    return true;
                }
            }

            return false;
        }).orElse(null);

        return foundBlockPos;
    }

    public void tick() {
        BlockPos sourcePos = getConsumableSourcePos();
        if (sourcePos == null) {
            mob.getWrapper().getBukkitEntity().setEatingHaystack(false);
            moveToTargetBlock();

            if (lastTickPos.equals(mob.position())) {
                if (++ticksSinceLastMove > 10) {
                    // move the horse further into the target block if it gets stuck
                    int xOffset = lastTickPos.x() - targetBlock.getX() > 0 ? -1 : 1;
                    int zOffset = lastTickPos.z() - targetBlock.getZ() > 0 ? -1 : 1;

                    mob.getNavigation().moveTo(targetBlock.getX() + xOffset, targetBlock.getY(), targetBlock.getZ() + zOffset, speedModifier);

                    ticksSinceLastMove = 0;
                }
            }
            else {
                lastTickPos = mob.position();
            }
            
        }
        else if (mob.getNavigation().isDone()) {
            mob.getLookControl().setLookAt(sourcePos.getX() + 0.5, sourcePos.getY() + 0.5, sourcePos.getZ() + 0.5, 2000, 2000);
            increaseStat(getStatFunction.getStat(), sourcePos);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(true);
        }
    }

    public void start() {
        mob.setUsingConsumingGoal(true);
        moveToTargetBlock();
    }

    private void moveToTargetBlock() {
        Path path = mob.getNavigation().createPath(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), 0);
        mob.getNavigation().moveTo(path, speedModifier);
    }

    public void stop() {
        mob.setUsingConsumingGoal(false);
        startConsumingLimit = randomizeStartConsumingLimit();
    }

    protected abstract double randomizeStartConsumingLimit();

    protected abstract BlockPos getConsumableSourcePos();

    protected abstract void increaseStat(Stat stat, BlockPos sourcePos);
}
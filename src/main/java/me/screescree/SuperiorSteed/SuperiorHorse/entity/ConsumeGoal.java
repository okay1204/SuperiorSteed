package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;
import java.util.function.Predicate;

import org.bukkit.World;
import org.bukkit.block.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public abstract class ConsumeGoal extends Goal {
    private final static int MAX_DISTANCE = 20;

    protected final SuperiorHorseEntity mob;
    private final double speedModifier;
    private Predicate<Block> desiredBlockPredicate;
    private Block targetBlock;
    private Vec3 lastTickPos;
    private int ticksSinceLastMove = 0;

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, double speedModifier, Predicate<Block> desiredBlockPredicate) {
        mob = superiorHorse;
        this.speedModifier = speedModifier;
        this.desiredBlockPredicate = desiredBlockPredicate;
        lastTickPos = mob.position();
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, double speedModifier) {
        this(superiorHorse, speedModifier, null);
    }

    protected void setDesiredBlockPredicate(Predicate<Block> desiredBlock) {
        this.desiredBlockPredicate = desiredBlock;
    }

    @Override
    public boolean canUse() {
        if (desiredBlockPredicate == null) {
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
                ticksSinceLastMove = 0;
            }
            
        }
        else if (mob.getNavigation().isDone()) {
            mob.getLookControl().setLookAt(sourcePos.getX() + 0.5, sourcePos.getY() + 0.5, sourcePos.getZ() + 0.5, 2000, 2000);
            increaseStat(sourcePos);
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
    }

    protected abstract BlockPos getConsumableSourcePos();

    protected abstract void increaseStat(BlockPos sourcePos);
}
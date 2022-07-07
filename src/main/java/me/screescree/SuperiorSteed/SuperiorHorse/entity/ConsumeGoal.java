package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;

import org.bukkit.block.Block;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.entity.blockfinder.BlockFinder;
import me.screescree.SuperiorSteed.superiorhorse.entity.blockfinder.StepResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public abstract class ConsumeGoal extends Goal {
    private final static int STEPS_PER_TICK = SuperiorSteed.getInstance().getConfig().getInt("pathfinding-steps-per-tick", 20);

    protected final SuperiorHorseEntity mob;
    private final double speedModifier;
    private Block targetBlock;
    private Vec3 lastTickPos;
    private int ticksSinceLastMove = 0;
    private BlockFinder blockFinder;

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, double speedModifier) {
        mob = superiorHorse;
        this.speedModifier = speedModifier;
        lastTickPos = mob.position();
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!canStart()) {
            blockFinder = null;
            return false;
        }

        if (blockFinder == null) {
            if (mob.getWrapper() == null) {
                return false;
            }

            blockFinder = new BlockFinder(mob.getWrapper(), this::isDesiredBlock);
        }

        for (int i = 0; i < STEPS_PER_TICK; i++) {
            StepResult stepResult = blockFinder.step();
            if (stepResult == StepResult.SUCCESS) {
                targetBlock = blockFinder.getFound();
                blockFinder = new BlockFinder(mob.getWrapper(), this::isDesiredBlock);
                return true;
            }
            else if (stepResult == StepResult.FAILURE) {
                blockFinder = new BlockFinder(mob.getWrapper(), this::isDesiredBlock);
                return false;
            }
        }

        // return false if blockfinder is still progress
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!canContinue()) {
            blockFinder = null;
            return false;
        }

        Path path = mob.getNavigation().getPath();
        if (path != null && path.isDone()) {
            blockFinder = null;
            return false;
        }

        for (int i = 0; i < STEPS_PER_TICK; i++) {
            StepResult stepResult = blockFinder.step();
            if (stepResult == StepResult.SUCCESS) {
                targetBlock = blockFinder.getFound();
                blockFinder = new BlockFinder(mob.getWrapper(), this::isDesiredBlock);
                return true;
            }
            else if (stepResult == StepResult.FAILURE) {
                blockFinder = new BlockFinder(mob.getWrapper(), this::isDesiredBlock);
                // we return true in case the horse is still on its path to the target block
                return true;
            }
        }

        // return true if blockfinder is still in progress
        return true;
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

    protected abstract boolean isDesiredBlock(Block block);

    protected abstract boolean canStart();

    protected abstract boolean canContinue();
}
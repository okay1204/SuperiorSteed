package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class ConsumeGoal extends Goal {
    protected final SuperiorHorseEntity mob;
    protected final double speedModifier;
    protected final GetStatPredicate statPredicate;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected BlockPos targetPos;
    private double startConsumingLimit;

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, GetStatPredicate statPredicate, double d0) {
        mob = superiorHorse;
        speedModifier = d0;
        this.statPredicate = statPredicate;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        randomizeStartConsumingLimit();
    }

    @Override
    public boolean canUse() {
        if (statPredicate.getStat().get() > startConsumingLimit) {
            return false;
        }

        BlockPos blockposition = lookForSuitablePos();
        if (blockposition != null) {
           posX = blockposition.getX();
           posY = blockposition.getY();
           posZ = blockposition.getZ();
           return true;
        }
        return false;
    }

    public boolean canContinueToUse() {
        BlockPos blockposition = lookForSuitablePos();
        if (blockposition == null) {
            return false;
        }
        else {
            posX = blockposition.getX();
            posY = blockposition.getY();
            posZ = blockposition.getZ();
        }

        return statPredicate.getStat().get() < 1.0;
    }

    public void tick() {
        BlockPos sourcePos = getConsumableSourcePos(mob.blockPosition());
        if (sourcePos == null) {
            mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(false);
        }
        else if (mob.getNavigation().isDone()) {
            mob.getLookControl().setLookAt(sourcePos.getX(), sourcePos.getY() - 5, sourcePos.getZ());
            statPredicate.getStat().add(0.01);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(true);
        }
    }

    public void start() {
        mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
    }

    public void stop() {
        randomizeStartConsumingLimit();
    }

    protected void randomizeStartConsumingLimit() {
        startConsumingLimit = mob.getRandom().nextDouble(0.9, 0.98);
    }

    protected abstract BlockPos lookForSuitablePos();

    protected abstract BlockPos getConsumableSourcePos(BlockPos blockPos);
}
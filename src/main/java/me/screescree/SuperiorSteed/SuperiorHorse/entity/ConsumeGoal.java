package me.screescree.SuperiorSteed.superiorhorse.entity;

import java.util.EnumSet;

import me.screescree.SuperiorSteed.superiorhorse.info.Stat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class ConsumeGoal extends Goal {
    protected final SuperiorHorseEntity mob;
    private final double speedModifier;
    private final GetStatPredicate statPredicate;
    private double posX;
    private double posY;
    private double posZ;
    private double startConsumingLimit;

    public ConsumeGoal(SuperiorHorseEntity superiorHorse, GetStatPredicate statPredicate, double d0) {
        mob = superiorHorse;
        speedModifier = d0;
        this.statPredicate = statPredicate;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        startConsumingLimit = randomizeStartConsumingLimit();
    }

    @Override
    public boolean canUse() {
        if (statPredicate.getStat() == null) {
            return false;
        }

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
        BlockPos sourcePos = getConsumableSourcePos();
        if (sourcePos == null) {
            mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(false);
        }
        else if (mob.getNavigation().isDone()) {
            mob.getLookControl().setLookAt(sourcePos.getX(), sourcePos.getY() - 5, sourcePos.getZ(), 2000, 2000);
            increaseStat(statPredicate.getStat(), sourcePos);
            mob.getWrapper().getBukkitEntity().setEatingHaystack(true);
        }
    }

    public void start() {
        mob.getNavigation().moveTo(posX, posY, posZ, speedModifier);
    }

    public void stop() {
        startConsumingLimit = randomizeStartConsumingLimit();
    }

    protected abstract double randomizeStartConsumingLimit();

    protected abstract BlockPos lookForSuitablePos();

    protected abstract BlockPos getConsumableSourcePos();

    protected abstract void increaseStat(Stat stat, BlockPos sourcePos);
}
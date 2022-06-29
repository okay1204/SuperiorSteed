package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

public class HorseBreedGoal extends Goal {
    private final TargetingConditions PARTNER_TARGETING;
    protected final SuperiorHorseEntity animal;
    protected final Level level;
    @Nullable
    protected SuperiorHorseEntity partner;
    private int loveTime;
    private final double speedModifier;

    public HorseBreedGoal(SuperiorHorseEntity var0, double var1) {
        this.animal = var0;
        this.level = var0.level;
        this.speedModifier = var1;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

        PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight().selector(this::canBreed);
    }

    private boolean canBreed(LivingEntity other) {
        SuperiorHorse horseWrapper = animal.getWrapper();

        SuperiorHorseEntity otherHorse = (SuperiorHorseEntity) other;
        SuperiorHorse otherHorseWrapper = otherHorse.getWrapper();

        if (horseWrapper.isMale() == otherHorseWrapper.isMale()) {
            return false;
        }

        if (horseWrapper.isMale() && !horseWrapper.isStallion()) {
            return false;
        }

        if (otherHorseWrapper.isMale() && !otherHorseWrapper.isStallion()) {
            return false;
        }

        // TODO add a check so that a pregnant female horse can't breed

        return true;
    }

    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
    }

    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0D) {
            this.breed();
        }

    }

    @Nullable
    private SuperiorHorseEntity getFreePartner() {
        List<SuperiorHorseEntity> var0 = this.level.getNearbyEntities(SuperiorHorseEntity.class, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
        double var1 = Double.MAX_VALUE;
        SuperiorHorseEntity var3 = null;
        Iterator<SuperiorHorseEntity> var5 = var0.iterator();

        while(var5.hasNext()) {
            SuperiorHorseEntity var6 = var5.next();
            if (this.animal.canMate(var6) && this.animal.distanceToSqr(var6) < var1) {
                var3 = var6;
                var1 = this.animal.distanceToSqr(var6);
            }
        }
    
        return var3;
    }

    protected void breed() {
        if (!animal.getWrapper().isMale()) {
            animal.getWrapper().becomePregnant();
        }
        else {
            partner.getWrapper().becomePregnant();
        }
        animal.resetLove();
        animal.setAge(6000);
        partner.resetLove();
        partner.setAge(6000);
    }
}

package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeFromHorse extends AvoidEntityGoal<SuperiorHorseEntity> {
    private SuperiorHorseEntity horse;

    public FleeFromHorse(SuperiorHorseEntity horse, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
        super(horse, SuperiorHorseEntity.class, livingEntity -> {
            SuperiorHorseEntity otherHorse = (SuperiorHorseEntity) livingEntity;
            return otherHorse.getTarget() != null && otherHorse.getTarget().equals(horse);
        }, maxDist, walkSpeedModifier, sprintSpeedModifier, var -> true);
        this.horse = horse;
    }

    @Override
    public boolean canUse() {
        SuperiorHorse wrapper = horse.getWrapper();

        if (wrapper.getAttackedByHorseTimer() > 0 && !wrapper.isAttackingBack()) {
            boolean canUse = super.canUse();
            if (canUse) {
                // reset timer if still being chased
                wrapper.setAttackedByHorseTimer(1200);
            }

            return canUse;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (horse.getWrapper().getAttackedByHorseTimer() <= 0) {
            return false;
        }

        return super.canContinueToUse();
    }
}

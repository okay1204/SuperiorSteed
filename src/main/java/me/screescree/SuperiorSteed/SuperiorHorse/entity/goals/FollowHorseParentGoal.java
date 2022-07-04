package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;

public class FollowHorseParentGoal extends FollowParentGoal {
    protected final SuperiorHorseEntity mob;

    public FollowHorseParentGoal(SuperiorHorseEntity horse, double speedModifier) {
        super(horse, speedModifier);
        mob = horse;
    }

    @Override
    public boolean canUse() {
        if (mob.isUsingConsumingGoal()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.isUsingConsumingGoal()) {
            return false;
        }
        return super.canContinueToUse();
    }
}

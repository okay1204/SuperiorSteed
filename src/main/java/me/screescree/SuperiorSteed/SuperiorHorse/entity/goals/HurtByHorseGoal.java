package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import org.bukkit.Particle;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.utils.ParticleUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class HurtByHorseGoal extends HurtByTargetGoal {
    SuperiorHorseEntity horse;

    public HurtByHorseGoal(SuperiorHorseEntity entitycreature) {
        super(entitycreature, new Class[0]);
        horse = entitycreature;
    }

    @Override
    public boolean canUse() {
        LivingEntity entityliving = horse.getLastHurtByMob();

        if (entityliving == null) {
            return false;
        }

        if (entityliving.getType() == EntityType.HORSE) {
            SuperiorHorse wrapper = horse.getWrapper();
            if (wrapper.getAttackedByHorseTimer() > 0 && wrapper.isAttackingBack()) {
                return super.canUse();
            }
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

    @Override
    public void start() {
        ParticleUtil.spawnParticles(Particle.DAMAGE_INDICATOR, horse.getBukkitEntity().getLocation());
        super.start();
    }
}

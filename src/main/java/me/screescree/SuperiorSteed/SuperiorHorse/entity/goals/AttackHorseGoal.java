package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import org.bukkit.Particle;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.utils.ParticleUtil;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class AttackHorseGoal extends NearestAttackableTargetGoal<SuperiorHorseEntity> {
    private final SuperiorHorseEntity horse;

    public AttackHorseGoal(SuperiorHorseEntity entityinsentient) {
        super(entityinsentient, SuperiorHorseEntity.class, true);
        this.horse = entityinsentient;
    }

    public double attackChance(double friendliness) {
        return Math.max(0, (-0.000047777777777778 * friendliness) + 0.000043);
    }

    @Override
    public boolean canUse() {
        SuperiorHorse wrapper = horse.getWrapper();

        if ((wrapper.isMadAtHorse() || horse.getRandom().nextDouble() < attackChance(wrapper.friendlinessStat().get())) && !wrapper.isLeashedByPlayer()) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!horse.getWrapper().isMadAtHorse()) {
            return false;
        }

        return super.canContinueToUse();
    }

    @Override
    public void start() {
        horse.getWrapper().setMadAtHorse(true);
        ParticleUtil.spawnParticles(Particle.VILLAGER_ANGRY, horse.getBukkitEntity().getLocation());
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
}

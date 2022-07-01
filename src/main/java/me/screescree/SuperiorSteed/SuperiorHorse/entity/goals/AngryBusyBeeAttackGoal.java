package me.screescree.SuperiorSteed.superiorhorse.entity.goals;

import org.bukkit.Particle;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.entity.SuperiorHorseEntity;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import me.screescree.SuperiorSteed.utils.ParticleUtil;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class AngryBusyBeeAttackGoal extends NearestAttackableTargetGoal<Player> {
    private final SuperiorHorseEntity horse;

    public AngryBusyBeeAttackGoal(SuperiorHorseEntity entityinsentient) {
        super(entityinsentient, Player.class, true);
        this.horse = entityinsentient;
    }

    @Override
    public boolean canUse() {
        SuperiorHorse wrapper = horse.getWrapper();
        // if the horse has the busy bee trait and has not been ridden for 48 hours, then it will attack players
        if (wrapper.getTraits().contains(Trait.BUSY_BEE) && ((wrapper.getLastRidden() > 3456000) || horse.getWrapper().isMadAtPlayer())) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!horse.getWrapper().isMadAtPlayer()) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        horse.getWrapper().setMadAtPlayer(true);
        ParticleUtil.spawnParticles(Particle.VILLAGER_ANGRY, horse.getBukkitEntity().getLocation());
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
}

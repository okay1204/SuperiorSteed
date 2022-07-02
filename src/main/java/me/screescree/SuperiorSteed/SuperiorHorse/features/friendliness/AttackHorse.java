package me.screescree.SuperiorSteed.superiorhorse.features.friendliness;

import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class AttackHorse implements Listener {

    private double attackBackChance(double friendliness) {
        return (-0.8 * friendliness) + 1.1;
    }

    @EventHandler
    public void onLeashHorse(PlayerLeashEntityEvent event) {
        if (event.getEntity().getType() == EntityType.HORSE && event.getLeashHolder().getType() == EntityType.PLAYER) {
            SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getEntity());
            superiorHorse.setMadAtHorse(false);
            superiorHorse.setAttackedByHorseTimer(0);
        }
    }

    @EventHandler
    public void onHorseAttack(EntityDamageByEntityEvent event) {
        // prevent horse from damaging player if the player is on the horse's back
        if (event.getEntity().getType() == EntityType.HORSE && event.getDamager().getType() == EntityType.HORSE) {
            Horse victimHorse = (Horse) event.getEntity();
            SuperiorHorse damager = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getDamager());

            // check if the horse died
            if (victimHorse.getHealth() - event.getFinalDamage() <= 0) {
                damager.setMadAtHorse(false);
            }
            else if (!victimHorse.isLeashed() || victimHorse.getLeashHolder().getType() != EntityType.PLAYER) {
                // make the victim either in fight or flight mode
                SuperiorHorse victim = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse(victimHorse);
                if (victim.getAttackedByHorseTimer() <= 0) {
                    Random random = ThreadLocalRandom.current();
                    if (random.nextDouble() < attackBackChance(victim.friendlinessStat().get())) {
                        victim.setAttackingBack(true);
                    }
                    else {
                        victim.setAttackingBack(false);
                    }

                }
                victim.setAttackedByHorseTimer(1200);
            }
        }
    }
}

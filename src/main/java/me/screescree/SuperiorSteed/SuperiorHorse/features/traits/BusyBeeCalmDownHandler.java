package me.screescree.SuperiorSteed.superiorhorse.features.traits;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.utils.ParticleUtil;

public class BusyBeeCalmDownHandler implements Listener {
    @EventHandler
    public void onHorseRide(EntityMountEvent event) {
        if (event.getMount().getType() == EntityType.HORSE && event.getEntity().getType() == EntityType.PLAYER) {
            Horse horse = (Horse) event.getMount();
            SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse(horse);
            if (superiorHorse.isMadAtPlayer()) {
                Random random = ThreadLocalRandom.current();
                if (random.nextDouble() < 0.1) {
                    ParticleUtil.spawnParticles(Particle.HEART, superiorHorse.getBukkitEntity().getLocation());
                    superiorHorse.setMadAtPlayer(false);
                }
                else {
                    Bukkit.getScheduler().runTaskLater(SuperiorSteed.getInstance(), () -> {
                        if (!superiorHorse.getBukkitEntity().isEmpty()) {
                            superiorHorse.getBukkitEntity().eject();
                        }
                        superiorHorse.getNMSEntity().makeMad();
                        ParticleUtil.spawnParticles(Particle.VILLAGER_ANGRY, superiorHorse.getBukkitEntity().getLocation());
                    }, random.nextInt(10, 40));
                }
            }   
        }
    }

    @EventHandler
    public void noDamageWhileMounted(EntityDamageByEntityEvent event) {
        // prevent horse from damaging player if the player is on the horse's back
        if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.HORSE) {
            if (event.getDamager().getPassengers().contains(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }
}

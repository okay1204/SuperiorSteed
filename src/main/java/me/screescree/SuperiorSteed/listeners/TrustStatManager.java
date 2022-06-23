package me.screescree.SuperiorSteed.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class TrustStatManager implements Listener {
    // Returns the chance of refusing to jump or be lead by a player
    private double rejectChance(double trust) {
        return Math.max(0, (-0.54 * trust) + 0.5);
    }

    @EventHandler
    public void onLeashHorse(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Horse && player.getInventory().getItem(event.getHand()).getType() == Material.LEAD) {
            SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getRightClicked());
            double trust = superiorHorse.trustStat().get();
            double rejectChance = rejectChance(trust);
            if (ThreadLocalRandom.current().nextDouble() < rejectChance) {
                player.sendMessage(Utils.colorize("&c" + superiorHorse.getName(20) + " resisted your attempt to leash it. &7(Trust: " + (int) Math.ceil(trust * 100) + "%)"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHorseJump(HorseJumpEvent event) {
        // Ignore if horse isn't on the ground
        if (!(event.getEntity().isOnGround())) {
            return;
        }

        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getEntity());
        
        double trust = superiorHorse.trustStat().get();
        double rejectChance = rejectChance(trust);

        if (ThreadLocalRandom.current().nextDouble() < rejectChance) {

            
            Player player = (Player) superiorHorse.getBukkitEntity().getPassengers().get(0);
            player.sendMessage(Utils.colorize("&c" + superiorHorse.getName(20) + " refused to jump. &7(Trust: " + (int) Math.ceil(trust * 100) + "%)"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            
            // cancel the jump by forcing the horse downward
            new BukkitRunnable() {
                @Override
                public void run() {
                    Vector currentVelocity = superiorHorse.getBukkitEntity().getVelocity();
                    superiorHorse.getBukkitEntity().setVelocity(new Vector(currentVelocity.getX(), -1000, currentVelocity.getZ()));
                }
            }.runTaskLater(SuperiorSteed.getInstance(), 1);
        }

    }

}

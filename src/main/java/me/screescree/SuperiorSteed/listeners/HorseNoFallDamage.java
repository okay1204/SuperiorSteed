package me.screescree.SuperiorSteed.listeners;

import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HorseNoFallDamage implements Listener {
    @EventHandler
    public void onHorseDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Horse && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }
}

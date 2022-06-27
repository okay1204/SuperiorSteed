package me.screescree.SuperiorSteed.superiorhorse.features.waterbravery;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

public class HurtInWater implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onHurtInWater(EntityDamageEvent event) {
        if (!(event.getEntity().getType() == EntityType.HORSE)) {
            return;
        }

        Horse horse = (Horse) event.getEntity();
        if (!horse.isInWater()) {
            return;
        }

        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse(horse);
        superiorHorse.waterBraveryStat().add(event.getFinalDamage() * -0.1);
    }
}

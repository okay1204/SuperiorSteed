package me.screescree.SuperiorSteed.superiorhorse.features.pregnancy;

import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.utils.Format;

public class NoRideWhenPregnant implements Listener {
    @EventHandler
    public void onPlayerMount(EntityMountEvent event) {
        if (event.getMount().getType() == EntityType.HORSE && event.getEntity().getType() == EntityType.PLAYER) {
            SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getMount());

            if (superiorHorse.isPregnant()) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                player.sendMessage(Format.colorize("&c" + superiorHorse.getName(20) + " is pregnant and cannot be ridden."));
                player.getWorld().playSound(superiorHorse.getBukkitEntity(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        }
    }
}

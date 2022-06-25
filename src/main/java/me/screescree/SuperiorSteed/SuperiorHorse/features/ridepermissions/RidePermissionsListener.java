package me.screescree.SuperiorSteed.superiorhorse.features.ridepermissions;

import org.bukkit.Sound;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;

public class RidePermissionsListener implements Listener {
    @EventHandler
    public void onHorseRide(EntityMountEvent event) {
        if (event.getMount() instanceof Horse && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // allow if the player has admin permissions
            if (player.hasPermission("superiorsteed.admin.rideallhorses")) {
                return;
            }

            AnimalTamer owner = ((Horse) event.getMount()).getOwner();
            // allow if the horse is owned the by the player
            if (owner.getUniqueId().equals(player.getUniqueId())) {
                return;
            }

            // allow if the player has the permission to ride the horse
            if (SuperiorSteed.getInstance().getDatabase().hasPermission(owner.getUniqueId(), player.getUniqueId())) {
                return;
            }

            // deny if all else fails
            event.setCancelled(true);
            player.sendMessage(Utils.colorize("&cYou do not have permission from &e" + owner.getName() + " &cto ride their horse."));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }
}

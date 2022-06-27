package me.screescree.SuperiorSteed.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;

public class HorseWarp implements Listener {
    private Map<Player, Horse> warpingPlayers = new HashMap<Player, Horse>();

    @EventHandler(ignoreCancelled = true)
    public void onWarpCommand(PlayerCommandPreprocessEvent event) {
        String commandName = event.getMessage().split(" ")[0].substring(1);
        if (!commandName.equals("warp") && !commandName.equals("essentials:warp")) {
            return;
        }
        
        if (event.getPlayer().isInsideVehicle() && event.getPlayer().getVehicle().getType() == EntityType.HORSE) {
            Player player = event.getPlayer();
            Horse horse = (Horse) player.getVehicle();
            warpingPlayers.put(player, horse);

            // Remove the player from the map after 2 seconds, in case the player doesn't warp.
            Bukkit.getScheduler().runTaskLater(SuperiorSteed.getInstance(), () -> {
                if (warpingPlayers.containsKey(player)) {
                    warpingPlayers.remove(player);
                }
            }, 40);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWarp(PlayerTeleportEvent event) {
        if (!warpingPlayers.containsKey(event.getPlayer())) {
            return;
        }

        Player player = event.getPlayer();
        Horse horse = warpingPlayers.get(player);
        warpingPlayers.remove(player);

        horse.eject();
        horse.teleport(event.getTo());

        Bukkit.getScheduler().runTaskLater(SuperiorSteed.getInstance(), () -> {
            horse.addPassenger(player);
        }, 10);
    }
}

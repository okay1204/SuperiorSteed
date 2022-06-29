package me.screescree.SuperiorSteed.superiorhorse.features.ageing;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class StopFeedGrowth implements Listener {
    private static HashSet<Material> feedableItems = new HashSet<Material>();
    static {
        feedableItems.add(Material.SUGAR);
        feedableItems.add(Material.WHEAT);
        feedableItems.add(Material.APPLE);
        feedableItems.add(Material.GOLDEN_CARROT);
        feedableItems.add(Material.GOLDEN_APPLE);
        feedableItems.add(Material.ENCHANTED_GOLDEN_APPLE);
        feedableItems.add(Material.HAY_BLOCK);
    }

    @EventHandler
    public void onPlayerFeedHorse(PlayerInteractEntityEvent event) {
        if (
            event.getRightClicked() instanceof Horse &&
            feedableItems.contains(event.getPlayer().getInventory().getItem(event.getHand()).getType())
        ) {
            Horse horse = (Horse) event.getRightClicked();

            if (!horse.isAdult()) {
                ((Horse) event.getRightClicked()).setAge(-24000);
            }
        }
    }
}

package me.screescree.SuperiorSteed.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import me.screescree.SuperiorSteed.superiorhorse.info.Seed;;

public class BrewingSeeds implements Listener {
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onClickBrewingSlot(InventoryClickEvent event) {
        if (event.getInventory() instanceof BrewerInventory && event.getView().getTopInventory().equals(event.getClickedInventory())) {
            BrewerInventory inventory = (BrewerInventory) event.getInventory();
            // If the slot is one of the three potion slots
            if (event.getSlot() >= 0 && event.getSlot() <= 2) {
                // If the item is a seed
                if (event.getCurrentItem().getType() == Material.AIR && event.getCursor() != null && Seed.MATERIALS.contains(event.getCursor().getType())) {
                    ItemStack droppedItem = event.getCursor().clone();
                    droppedItem.setAmount(1);
                    event.getCursor().setAmount(event.getCursor().getAmount() - 1);

                    inventory.setItem(event.getSlot(), droppedItem);
                    event.setCursor(event.getCursor());
                    event.setCancelled(true);
                }
            }
        }
    }
}

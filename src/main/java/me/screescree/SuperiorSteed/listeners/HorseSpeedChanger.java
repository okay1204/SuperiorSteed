package me.screescree.SuperiorSteed.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.SpeedLevel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HorseSpeedChanger implements Listener {
    public HorseSpeedChanger() {
        // Create a task to loop though all 
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    sendHorseSpeedActionBar(player);
                }
            }
        }.runTaskTimer(SuperiorSteed.getInstance(), 1, 20);
    }

    @EventHandler
    public void onPlayerChangeSpeed(PlayerInteractEvent event) {
        // ignore if the item is not a stick
        if (event.getItem() == null || event.getItem().getType() != Material.STICK) {
            return;
        }
        // ignore second event call if player has stick in both hands
        if (event.getHand() == EquipmentSlot.OFF_HAND && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
            return;
        }
        // ignore if player is not riding a horse
        Entity vehicle = event.getPlayer().getVehicle();
        if (!(vehicle instanceof Horse)) {
            return;
        }
        
        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) vehicle);

        if (!superiorHorse.getBukkitEntity().isTamed()) {
            return;
        }
        
        superiorHorse.cycleSpeedLevel();
        sendHorseSpeedActionBar(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDismount(EntityDismountEvent event) {
        Entity dismounted = event.getDismounted();
        if (!(dismounted instanceof Horse) || !(event.getEntity() instanceof Player)) {
            return;
        }

        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) dismounted);
        superiorHorse.resetSpeedLevel();
    }

    private void sendHorseSpeedActionBar(Player player) {
        // ignore if player is not riding a horse
        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof Horse)) {
            return;
        }
        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) vehicle);

        if (!superiorHorse.getBukkitEntity().isTamed()) {
            return;
        }

        SpeedLevel speedLevel = superiorHorse.getSpeedLevel();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(speedLevel.getColor() + "Speed: " + speedLevel.getName()));
    }
}

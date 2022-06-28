package me.screescree.SuperiorSteed.superiorhorse.features.grooming;

import java.util.Random;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.netty.util.internal.ThreadLocalRandom;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.utils.Format;

public class GroomItemUse implements Listener {
    @EventHandler
    public void onGroomHorse(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Horse) {
            ItemStack item = player.getInventory().getItem(event.getHand());

            if (item.getType() != Material.BRICK) {
                return;
            }

            ItemMeta meta = item.getItemMeta();
            int customModelData = meta.getCustomModelData();

            if (!(customModelData >= 1 && customModelData <= SuperiorHorse.GROOMING_ITEM_AMOUNT)) {
                return;
            }

            event.setCancelled(true);

            SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse((Horse) event.getRightClicked());

            if (superiorHorse.getGroomExpireTimer() > 0) {
                player.sendMessage(Format.colorize("&c" + superiorHorse.getName(20) + " has already been groomed today."));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            if (superiorHorse.getGroomedBy().contains(customModelData)) {
                player.sendMessage(Format.colorize("&c" + superiorHorse.getName(20) + " has already been groomed with this item."));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            Set<Integer> groomedBy = superiorHorse.getGroomedBy();
            groomedBy.add(customModelData);
            superiorHorse.setGroomedBy(groomedBy);
            
            superiorHorse.friendlinessStat().add(0.02);
            
            if (superiorHorse.verifyGrooming()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage(Format.colorize("&a" + superiorHorse.getName(20) + " has been groomed by all " + SuperiorHorse.GROOMING_ITEM_AMOUNT + " items for today!"));
            }
            else {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }

            Random random = ThreadLocalRandom.current();
            for (int i = 0; i < 10; i++) {
                double randX = random.nextDouble(-1, 1);
                double randY = random.nextDouble(1, 2);
                double randZ = random.nextDouble(-1, 1);
                player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, superiorHorse.getBukkitEntity().getLocation().add(randX, randY, randZ), 1);
            }
        }
    }
}

package me.screescree.SuperiorSteed.superiorhorse.features.speedchanger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.SpeedLevel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Funcs {
    public static void sendHorseSpeedActionBar(Player player) {
        // ignore if player is not riding a horse
        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof Horse)) {
            return;
        }

        Horse horse = (Horse) vehicle;
        
        if (!horse.isTamed()) {
            return;
        }
        SuperiorHorse superiorHorse = SuperiorSteed.getInstance().getHorseManager().getSuperiorHorse(horse);

        SpeedLevel speedLevel = superiorHorse.getSpeedLevel();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(speedLevel.getColor() + "Speed: " + speedLevel.getName()));
    }
}

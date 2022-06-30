package me.screescree.SuperiorSteed.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.info.SuperiorHorseInfo;
import me.screescree.SuperiorSteed.superiorhorse.info.Trait;
import me.screescree.SuperiorSteed.utils.AgeTimeSplitter;
import me.screescree.SuperiorSteed.utils.Format;
import me.screescree.SuperiorSteed.utils.RayTraceUtils;

public class HorseStats extends CustomCommand {
    public HorseStats() {
        super("horsestats");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        else {
            sender.sendMessage(Format.colorize("&cThis command can only be used by a player."));
            return true;
        }
        
        Horse targetHorse = RayTraceUtils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Format.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        String ownerText = superiorHorse.getOwner() != null ? "&6(owned by " + superiorHorse.getOwner().getName() + ")" : "&6(unowned)";
        player.sendMessage(Format.colorize("&7---------------- &e" + superiorHorse.getName(20) + "'s stats " + ownerText +  " &7----------------"));

        String ageType;
        if (superiorHorse.getAge() < SuperiorHorseInfo.AGE_ADULT) {
            ageType = "Foal";
        }
        else if (superiorHorse.getAge() < SuperiorHorseInfo.AGE_SENIOR) {
            ageType = "Adult";
        }
        else {
            ageType = "Senior";
        }

        player.sendMessage(Format.colorize("&5Age: &d" + ageType));
        player.sendMessage(ChatColor.LIGHT_PURPLE + new AgeTimeSplitter(superiorHorse.getAge()).formatString());

        String stats = "";
        stats += addStatMessage("Hunger", superiorHorse.hungerStat().get());
        stats += addStatMessage("Hydration", superiorHorse.hydrationStat().get());
        stats += addStatMessage("Trust", superiorHorse.trustStat().get());
        stats += "\n";
        stats += addStatMessage("Friendliness", superiorHorse.friendlinessStat().get());
        stats += addStatMessage("Comfortability", superiorHorse.comfortabilityStat().get());
        stats += "\n";
        stats += addStatMessage("Water Bravery", superiorHorse.waterBraveryStat().get());
        
        player.sendMessage(stats);
        player.sendMessage(Format.colorize("&bGrooming Status: " + (superiorHorse.isFinishedGrooming() ? "&aGroomed" : "&cNot Groomed")));
        player.sendMessage("\n");
        
        String horseType;
        if (superiorHorse.isMale()) {
            horseType = Format.colorize("&9♂ Male");

            if (superiorHorse.isStallion()) {
                horseType += " (Stallion)";
            }
            else {
                horseType += " (Gelding)";
            }
        }
        else {
            horseType = Format.colorize("&d♀ Female");
        }
        player.sendMessage(horseType);

        Set<Trait> traits = superiorHorse.getTraits();
        String traitString = Format.colorize("&3Traits: ");
        if (!traits.isEmpty()) {
            for (Trait trait : traits) {
                traitString += (trait.isPositive() ? ChatColor.GREEN : ChatColor.RED) + trait.getFormalName() + ChatColor.GRAY + ", ";
            }

            // remove last comma
            traitString = traitString.substring(0, traitString.length() - 4);
        }
        else {
            traitString += Format.colorize("&7None");
        }

        player.sendMessage(traitString);

        if (superiorHorse.isPregnant()) {
            player.sendMessage("\n");
            player.sendMessage(Format.colorize("&5Pregnant for: &d" + new AgeTimeSplitter(superiorHorse.getPregnancyTimer()).formatString()));
            player.sendMessage(addStatMessage("Pregnancy Complication", superiorHorse.pregnancyComplicationStat().get()));
        }

        return true;
    }

    private String addStatMessage(String name, double value) {
        return addStatMessage(name, value, 4);
    }

    private String addStatMessage(String name, double value, int trailingSpaces) {
        String colorCode;
        if (value <= 0.19) {
            colorCode = "&c";
        }
        else if (value <= 0.59) {
            colorCode = "&e";
        }
        else {
            colorCode = "&a";
        }

        int percentValue = (int) Math.ceil(value * 100);
        String outputString = Format.colorize(colorCode + name + ": &f" + percentValue + "%");
        
        String meterString = colorCode;
        for (int i = 0; i < percentValue / 10; i++) {
            meterString += "█";
        }
        meterString += "&7";
        for (int i = 0; i < (100 - percentValue) / 10; i++) {
            meterString += "░";
        }

        outputString += " " + Format.colorize(meterString);
        for (int i = 0; i < trailingSpaces; i++) {
            outputString += " ";
        }

        return outputString;
    }
}

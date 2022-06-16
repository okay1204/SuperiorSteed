package me.screescree.SuperiorSteed.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.CustomCommand;
import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;

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
            sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
            return true;
        }
        
        Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Utils.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        player.sendMessage(Utils.colorize("&7---------------- &e" + superiorHorse.getName(20) + "'s stats &7----------------"));
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
        player.sendMessage("\n");

        String horseType;
        if (superiorHorse.isMale()) {
            horseType = Utils.colorize("&9♂ Male");

            if (superiorHorse.isStallion()) {
                horseType += " (Stallion)";
            }
            else {
                horseType += " (Gelding)";
            }
        }
        else {
            horseType = Utils.colorize("&d♀ Female");
        }
        player.sendMessage(horseType);

        return true;
    }

    private String addStatMessage(String name, double value) {
        return addStatMessage(name, value, 4);
    }

    private String addStatMessage(String name, double value, int trailingSpaces) {
        String colorCode;
        if (value < 0.2) {
            colorCode = "&c";
        }
        else if (value < 0.6) {
            colorCode = "&e";
        }
        else {
            colorCode = "&a";
        }

        int percentValue = (int) Math.ceil(value * 100);
        String outputString = Utils.colorize(colorCode + name + ": &f" + percentValue + "%");
        
        String meterString = colorCode;
        for (int i = 0; i < percentValue / 10; i++) {
            meterString += "█";
        }
        meterString += "&7";
        for (int i = 0; i < (100 - percentValue) / 10; i++) {
            meterString += "░";
        }

        outputString += " " + Utils.colorize(meterString);
        for (int i = 0; i < trailingSpaces; i++) {
            outputString += " ";
        }

        return outputString;
    }
}

package me.screescree.SuperiorSteed.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;

public class HorseStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SuperiorSteed plugin = SuperiorSteed.getInstance();
        
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
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        player.sendMessage(Utils.colorize("&7---------------- &e" + superiorHorse.getName(20) + "'s stats &7----------------"));
        String firstStatLine = "";
        firstStatLine += addStatMessage("Hunger", superiorHorse.hungerStat().get());
        firstStatLine += addStatMessage("Trust", superiorHorse.trustStat().get());
        firstStatLine += addStatMessage("Friendliness", superiorHorse.friendlinessStat().get());
        String secondStatLine = Utils.colorize("&f          ");
        secondStatLine += addStatMessage("Comfortability", superiorHorse.comfortabilityStat().get());
        secondStatLine += addStatMessage("Water Bravery", superiorHorse.waterBraveryStat().get());

        player.sendMessage(firstStatLine);
        player.sendMessage(secondStatLine);

        return true;
    }

    private String addStatMessage(String name, double value) {
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

        outputString += " " + Utils.colorize(meterString) + "    ";
        return outputString;
    }
}
package me.screescree.SuperiorSteed.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;
import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorseInfo;

public class UpdateHorse implements CommandExecutor, TabCompleter {
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

        if (args.length < 1) {
            return false;
        }
        else if (args[0].equals("stat")) {
            if (args.length < 3) {
                return false;
            }

            String statName = args[1];
            boolean foundStatName = false;
            for (String stat : SuperiorHorseInfo.DEFAULT_MAP.keySet()) {
                if (stat.equals(statName)) {
                    foundStatName = true;
                    break;
                }
            }
            if (!foundStatName) {
                sender.sendMessage(Utils.colorize("&cInvalid stat name."));
                return true;
            }

            int statValue;
            try {
                statValue = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Utils.colorize("&cValue should be an integer from 0 to 100."));
                return true;
            }

            if (statValue < 0 || statValue > 100) {
                sender.sendMessage(Utils.colorize("&cValue should be an integer from 0 to 100."));
                return true;
            }

            Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);
            if (targetHorse == null) {
                sender.sendMessage(Utils.colorize("&cYou must be riding or looking at a horse."));
                return true;
            }
            SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

            superiorHorse.getStat(statName).set(statValue / 100.0);
            sender.sendMessage(Utils.colorize("&aUpdated &2" + statName + " &aof &2" + superiorHorse.getName(20) + " &ato &f" + statValue + "%"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return List.of("stat", "looks");

        if (args.length == 2) {
            if (args[0].equals("stat"))
                return List.of("hunger", "trust", "friendliness", "comfortability", "waterbravery");
            else if (args[0].equals("looks"))
                return List.of("color", "style");
        }

        if (args.length == 3) {
            if (args[2].equals("color"))
                return List.of("black", "brown", "chestnut", "creamy", "darkbrown", "gray", "white");
            else if (args[2].equals("style"))
                return List.of("blackdots", "none", "white", "whitedots", "whitefield");
        }

        return List.of();
    }
}

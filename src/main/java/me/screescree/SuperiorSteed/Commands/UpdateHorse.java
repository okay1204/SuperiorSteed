package me.screescree.SuperiorSteed.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import me.screescree.SuperiorSteed.SuperiorSteed;
import me.screescree.SuperiorSteed.Utils;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;

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

        Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);
        if (targetHorse == null) {
            sender.sendMessage(Utils.colorize("&cYou must be riding or looking at a horse."));
            return true;
        }
        SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse(targetHorse);

        if (args.length < 1) {
            return false;
        }
        else if (args[0].equals("stat")) {
            if (args.length < 3) {
                return false;
            }

            String statName = args[1];

            if (!SuperiorHorseInfo.STAT_NAMES.contains(statName)) {
                player.sendMessage(Utils.colorize("&cInvalid stat name."));
                return true;
            }

            int statValue;
            try {
                statValue = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(Utils.colorize("&cValue should be an integer from 0 to 100."));
                return true;
            }

            if (statValue < 0 || statValue > 100) {
                player.sendMessage(Utils.colorize("&cValue should be an integer from 0 to 100."));
                return true;
            }

            superiorHorse.getStat(statName).set(statValue / 100.0);
            player.sendMessage(Utils.colorize("&aUpdated &2" + statName + " &aof &2" + superiorHorse.getName(20) + " &ato &f" + statValue + "%"));
        }
        else if (args[0].equals("looks")) {
            if (args.length < 3) {
                return false;
            }
        
            if (args[1].equals("color")) {
                Horse.Color color;
                try {
                    color = Horse.Color.valueOf(args[2].toUpperCase());
                }
                catch (IllegalArgumentException e) {
                    player.sendMessage(Utils.colorize("&cInvalid color."));
                    return true;
                }

                superiorHorse.getBukkitEntity().setColor(color);
                player.sendMessage(Utils.colorize("&aUpdated &2color &aof &2" + superiorHorse.getName(20) + " &ato &f" + args[2].toLowerCase() + "&a."));
            }
            else if (args[1].equals("style")) {
                Horse.Style style;
                try {
                    style = Horse.Style.valueOf(args[2].toUpperCase());
                }
                catch (IllegalArgumentException e) {
                    player.sendMessage(Utils.colorize("&cInvalid style."));
                    return true;
                }

                superiorHorse.getBukkitEntity().setStyle(style);
                player.sendMessage(Utils.colorize("&aUpdated &2style &aof &2" + superiorHorse.getName(20) + " &ato &f" + args[2].toLowerCase() + "&a."));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return List.of("stat", "looks");

        if (args.length == 2) {
            if (args[0].equals("stat"))
                return SuperiorHorseInfo.STAT_NAMES;
            else if (args[0].equals("looks"))
                return List.of("color", "style");
        }

        if (args.length == 3) {
            if (args[1].equals("color"))
                return List.of("black", "brown", "chestnut", "creamy", "dark_brown", "gray", "white");
            else if (args[1].equals("style"))
                return List.of("black_dots", "none", "white", "white_dots", "whitefield");
        }

        return List.of();
    }
}

package me.screescree.SuperiorSteed;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;
import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorseInfo;
public class CommandListener implements CommandExecutor, TabCompleter {
    private SuperiorSteed plugin;
    private final List<String> PERSISTENT_TYPES = List.of("double");

    public CommandListener(SuperiorSteed plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName();

        Player player;
        if (commandName.equals("horsestats")) {
            // TODO implement checking for horse player is riding on
            // for now it checks the horse that is being looked at
            
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
        }
        // ADMIN COMMANDS
        else if (commandName.equals("summonhorse")) {
            // TODO add ability to change spawn location, and horse settings
            player = (Player) sender;

            plugin.getHorseManager().newSuperiorHorse(player.getLocation());
            plugin.getLogger().info("Summoned a horse!");
        }
        else if (commandName.equals("updatehorse")) {
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
        }
        // DEBUG COMMANDS
        else if (commandName.equals("horsecache")) {
            sender.sendMessage(Utils.colorize("&aHorse cache:"));
            String cacheString = plugin.getHorseManager().getCache().toString();
            if (cacheString.length() > 255) {
                cacheString = cacheString.substring(0, 255);
            }
            sender.sendMessage(plugin.getHorseManager().getCache().toString());
            sender.sendMessage("Length: " + plugin.getHorseManager().getCache().size());
        }
        else if (commandName.equals("readpersistentdata")) {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            else {
                sender.sendMessage(Utils.colorize("&cThis command can only be used by a player."));
                return true;
            }

            if (args.length < 2) {
                return false;
            }

            String type = args[0];
            if (!PERSISTENT_TYPES.contains(type)) {
                sender.sendMessage(Utils.colorize("&cInvalid type."));
                return true;
            }

            String key = args[1];
            Horse targetHorse = Utils.getRiddenOrLookedAtHorse(player);

            PersistentDataContainer container = targetHorse.getPersistentDataContainer();

            if (type.equals("double")) {
                Double value = container.get(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
                sender.sendMessage(Utils.colorize("&a" + key + ": &f" + (value != null ? value.toString() : "null")));
            }
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("updatehorse")) {
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
        }
        else if (commandName.equals("readpersistentdata")) {
            if (args.length == 1)
                return PERSISTENT_TYPES;
        }

        return List.of();
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

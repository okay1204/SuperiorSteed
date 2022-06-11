package me.screescree.SuperiorSteed;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import me.screescree.SuperiorSteed.SuperiorHorse.SuperiorHorse;
public class CommandListener implements CommandExecutor, TabCompleter {
    private SuperiorSteed plugin;

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

            RayTraceResult raytraceResult = player.getWorld().rayTraceEntities(
                player.getLocation().add(0, 1, 0),
                player.getLocation().getDirection(),
                10,
                (Entity e) -> e instanceof CraftHorse
            );

            if (raytraceResult == null) {
                sender.sendMessage(Utils.colorize("&cYou are not looking at a horse."));
                return true;
            }

            Entity entity = raytraceResult.getHitEntity();
            SuperiorHorse superiorHorse = plugin.getHorseManager().getSuperiorHorse((Horse) entity);

            String horseName = superiorHorse.getBukkitEntity().getCustomName() != null ? superiorHorse.getBukkitEntity().getCustomName() : "Horse";
            // limit horse name to 20 characters
            if (horseName.length() > 20) {
                horseName = horseName.substring(0, 20);
            }

            player.sendMessage(Utils.colorize("&7---------------- &e" + horseName + "'s stats &7----------------"));
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
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        return new ArrayList<>();
    }

    private String addStatMessage(String name, double value) {
        int percentValue = (int) Math.ceil(value * 100);
        String outputString = Utils.colorize("&a" + name + ": &f" + percentValue + "%");
        
        String meterString = "&a";
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
